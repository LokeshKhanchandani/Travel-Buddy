package android.com.avishkar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by lokesh on 19/9/18.
 */

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.RecyclerViewHolder> {
    public Context mContext;
    public Days current;
    public ArrayList<Days> mDays;
    public String memail;

    DaysAdapter(Context context, ArrayList<Days> days,String email){
        mContext=context;
        mDays=days;
        memail=email;
    }
    @Override
    public DaysAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.days_item,parent,false);
        return  new DaysAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        current=mDays.get(position);
        //Toast.makeText(mContext,current.editable+"",Toast.LENGTH_SHORT).show();
        int x=position+1;
        holder.dayNo.setText(x+"");

        if(current.editable==false){
            holder.save.setVisibility(View.INVISIBLE);
            holder.expenses.setText(current.expenses);
            holder.description.setText(current.description);
            holder.expenses.setEnabled(false);
            holder.description.setEnabled(false);
        }
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.description=holder.description.getText().toString();
                String y=holder.expenses.getText().toString();
                current.expenses=y;
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                final DatabaseReference myRef=database.getReference().child("users").child(memail).child("ongoingTrip").child("current");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CurrentTour temp=dataSnapshot.getValue(CurrentTour.class);
                        ArrayList<Days>storageList=temp.days;
                        holder.save.setVisibility(View.INVISIBLE);
                        holder.expenses.setEnabled(false);
                        holder.description.setEnabled(false);
                        current.editable=false;
                        storageList.add(current);
                        temp.days=storageList;
                        myRef.setValue(temp);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView dayNo;
        public EditText description,expenses;
        public Button save;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            dayNo=itemView.findViewById(R.id.dayindex);
            description=itemView.findViewById(R.id.description);
            expenses=itemView.findViewById(R.id.expenses);
            save=itemView.findViewById(R.id.save);
        }
    }
}