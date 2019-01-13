package android.com.avishkar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by UTSAV JAIN on 9/20/2018.
 */

public class TripListAdapter extends ArrayAdapter<CurrentTour> {

    Context mContext;
    private String reducedemail;
    public TripListAdapter(@NonNull Context context, int resource, ArrayList<CurrentTour> currentTours,String email) {
        super(context, 0, currentTours);
        mContext = context;
        reducedemail=email;
    }
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private static class ViewHolder {
        TextView titleView, sourcef, destf, dateview;
        ImageView background;
        TextView titlef,datef,budgetf,statef,itirenary,head,duration;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FoldingCell listItem = (FoldingCell) convertView;
        ViewHolder viewHolder;
        final CurrentTour tour = (CurrentTour) getItem(position);
        if (listItem == null) {
            viewHolder = new ViewHolder();
            listItem = (FoldingCell) LayoutInflater.from(mContext)
                    .inflate(R.layout.ongoing_cell, parent, false);
            viewHolder.titleView = (TextView) listItem.findViewById(R.id.title);
            viewHolder.sourcef = (TextView) listItem.findViewById(R.id.sourcef);
            viewHolder.destf = (TextView) listItem.findViewById(R.id.destinationf);
            viewHolder.dateview = (TextView) listItem.findViewById(R.id.datef);
            viewHolder.datef=(TextView)listItem.findViewById(R.id.date);
            viewHolder.budgetf=(TextView)listItem.findViewById(R.id.budgetf);
            viewHolder.statef=(TextView)listItem.findViewById(R.id.statef);
            viewHolder.itirenary=(TextView)listItem.findViewById(R.id.itirenary_list);
            viewHolder.head=(TextView)listItem.findViewById(R.id.content_request_btn);
            viewHolder.titlef=(TextView)listItem.findViewById(R.id.titlef);
            viewHolder.background=(ImageView)listItem.findViewById(R.id.head_image);
            viewHolder.duration=(TextView)listItem.findViewById(R.id.daysf);
            listItem.setTag(viewHolder);
        }
        else {

            if (unfoldedIndexes.contains(position)) {
                listItem.unfold(true);
            } else {
                listItem.fold(true);
            }
            viewHolder=(ViewHolder)listItem.getTag();
        }

        if(tour==null)
            return listItem;

        viewHolder.titleView.setText(tour.title);
        viewHolder.sourcef.setText(tour.source);
        viewHolder.destf.setText(tour.destination);
        String formatedDate = (String) DateFormat.format("dd", tour.startDate) + "," +
                (String) DateFormat.format("MMM", tour.startDate) + "," +
                (String) DateFormat.format("yyyy", tour.startDate);
        viewHolder.dateview.setText(formatedDate + "");
        viewHolder.titlef.setText(tour.title);
        viewHolder.duration.setText(tour.tripDuration);
        viewHolder.datef.setText(formatedDate + "");
        viewHolder.budgetf.setVisibility(View.VISIBLE);
        viewHolder.statef.setVisibility(View.VISIBLE);
        viewHolder.budgetf.setText("Rs"+tour.budget);
        viewHolder.itirenary.setText(tour.itirenary);
        if(tour.state==false) {
            viewHolder.statef.setText("OnGoing");
            viewHolder.head.setText("Add experience");
            viewHolder.head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent topresenttrip=new Intent(mContext,PresentTrip.class);
                    topresenttrip.putExtra("topresenttrip",tour);
                    topresenttrip.putExtra("email",reducedemail);
                    mContext.startActivity(topresenttrip);
                }
            });
        }
        else {
            viewHolder.statef.setText("Past");
            viewHolder.head.setText("Memories");
            viewHolder.head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent topresenttrip=new Intent(mContext,PresentTrip.class);
                    topresenttrip.putExtra("topresenttrip",tour);
                    topresenttrip.putExtra("email",reducedemail);
                    Toast.makeText(mContext,tour.days.size()+"",Toast.LENGTH_LONG).show();
                    mContext.startActivity(topresenttrip);
                }
            });
        }

        if(position%3==0)
            viewHolder.background.setImageResource(R.drawable.header);
        else if(position%3==1)
            viewHolder.background.setImageResource(R.drawable.mountain1);
        else
            viewHolder.background.setImageResource(R.drawable.mountain2);
        return listItem;
    }
    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }
}