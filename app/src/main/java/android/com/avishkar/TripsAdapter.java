package android.com.avishkar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
 * Created by UTSAV JAIN on 9/18/2018.
 */

public class TripsAdapter extends ArrayAdapter<Trips> {
    int resource;
    Context context;

    public TripsAdapter(@NonNull Context context, int res, ArrayList<Trips> Trips) {
        super(context, res, Trips);
        this.context = context;
        this.resource = res;
    }

    private static class ViewHolder{
        TextView source, destination, days, itirenary, date;
        TextView head,sourcef,destf;
        ImageView background;
    }
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Trips trips = (Trips) getItem(position);
        FoldingCell listItem = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (listItem == null) {
            viewHolder=new ViewHolder();
            LayoutInflater li;
            li = LayoutInflater.from(context);
            listItem = (FoldingCell) li.inflate(R.layout.cell, null);
            viewHolder.source = (TextView) listItem.findViewById(R.id.source_tv);
            viewHolder.destination = (TextView) listItem.findViewById(R.id.destination_tv);
            viewHolder.days = (TextView) listItem.findViewById(R.id.daysf);
            viewHolder.itirenary = (TextView) listItem.findViewById(R.id.itirenary_list);
            viewHolder.date = (TextView) listItem.findViewById(R.id.datef);
            viewHolder.sourcef = (TextView) listItem.findViewById(R.id.sourcef);
            viewHolder.destf = (TextView) listItem.findViewById(R.id.destinationf);
            viewHolder.head=(TextView)listItem.findViewById(R.id.content_request_btn);
            viewHolder.background=(ImageView)listItem.findViewById(R.id.head_image);
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
        if(trips==null)
            return listItem;

        viewHolder.source.setText(trips.getSource());
        viewHolder.destination.setText(trips.getDestination());
        viewHolder.days.setText(String.valueOf(trips.getDays()));
        viewHolder.itirenary.setText(trips.getItirenrary());
        viewHolder.date.setText(trips.getDate());
        viewHolder.sourcef.setText(trips.getSource());
        viewHolder.destf.setText(trips.getDestination());
        if(position%3==0)
            viewHolder.background.setImageResource(R.drawable.header);
        else if(position%3==1)
            viewHolder.background.setImageResource(R.drawable.mountain1);
        else
            viewHolder.background.setImageResource(R.drawable.mountain2);


        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double soLat = trips.getsLat();
                double soLng = trips.getsLng();
                double deLat = trips.getdLat();
                double deLng = trips.getdLng();
                Intent routeIntent = new Intent(context,MyMapLocation.class);
                Log.e(" DLat",deLat+""+deLng);
                routeIntent.putExtra("feature",101);
                routeIntent.putExtra("source_latitude",soLat);
                routeIntent.putExtra("source_longitude",soLng);
                routeIntent.putExtra("destination_latitude",deLat);
                routeIntent.putExtra("destination_longitude",deLng);
                context.startActivity(routeIntent);

            }
        });
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