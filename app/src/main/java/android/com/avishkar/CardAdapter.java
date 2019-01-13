package android.com.avishkar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lokesh on 1/9/18.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.RecyclerViewHolder> {
    private Context mContext;
    private ArrayList<Tours> mTours;

    CardAdapter(Context context, ArrayList<Tours> tours){
        mContext=context;
        mTours=tours;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(mContext).inflate(R.layout.card_item,parent,false);
        return  new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Tours curr=mTours.get(position);
        switch(position)
        {
            case 0:
                holder.title.setText("current location...");
                holder.place.setText(curr.city);
                holder.address.setText(curr.address);
                holder.latitude.setText(curr.latitude+"");
                holder.longitude.setText(curr.longitude+"");
                holder.iv.setVisibility(View.INVISIBLE);
                holder.ratings.setVisibility(View.INVISIBLE);
                holder.draw.setVisibility(View.INVISIBLE);
                holder.temp.setVisibility(View.INVISIBLE);
                holder.lv.setBackgroundResource(R.drawable.back1);
                break;

            case 1://weather
                holder.title.setText("Weather");
                holder.place.setText(curr.city);
                holder.address.setText(curr.address);
                holder.latitude.setText(curr.latitude+"");
                holder.longitude.setText(curr.longitude+"");
                holder.iv.setVisibility(View.INVISIBLE);
                holder.ratings.setVisibility(View.INVISIBLE);
                holder.draw.setVisibility(View.INVISIBLE);
                holder.temp.setVisibility(View.INVISIBLE);
                holder.lv.setBackgroundResource(R.drawable.weather);
                break;
            default:
                holder.title.setText("Tourist places near you...");
                holder.place.setText(curr.city);
                holder.address.setText(curr.address);
                holder.latitude.setText(curr.latitude+"");
                holder.longitude.setText(curr.longitude+"");
                holder.iv.setVisibility(View.INVISIBLE);
                holder.ratings.setRating((float) curr.rating);
                holder.draw.setVisibility(View.INVISIBLE);
                holder.temp.setVisibility(View.INVISIBLE);
                holder.lv.setBackgroundColor(0xFFBE5B5B);
                holder.lv.setBackgroundResource(R.drawable.back2);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTours.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView place,title,address,opening,latitude,longitude;
        ImageView iv,temp,draw;
        LinearLayout lv;
        RatingBar ratings;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            place=itemView.findViewById(R.id.place);
            address=itemView.findViewById(R.id.address);
            opening=itemView.findViewById(R.id.open);
            latitude=itemView.findViewById(R.id.lat);
            longitude=itemView.findViewById(R.id.lon);
            ratings=(RatingBar) itemView.findViewById(R.id.rating);
            temp=itemView.findViewById(R.id.humi);
            iv=itemView.findViewById(R.id.cloud); 
            draw=itemView.findViewById(R.id.thermo);
            lv=(LinearLayout) itemView.findViewById(R.id.card);
        }
    }
}
