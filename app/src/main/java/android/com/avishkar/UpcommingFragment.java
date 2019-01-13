package android.com.avishkar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by UTSAV JAIN on 9/16/2018.
 */

public class UpcommingFragment extends Fragment {
    private FloatingActionButton addButton;
    public  String email;
    TripsAdapter adapter;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upcomming_fragment,container,false);
        final ListView list = view.findViewById(R.id.upcomming_list);
        addButton = view.findViewById(R.id.addButton);
        email = ((Dashboard)getActivity()).getEmail();
        final LinearLayout linearLayout = view.findViewById(R.id.not_found_upcoming);
        final ArrayList<Trips> tripDataList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Query query = firebaseDatabase.getReference().child("users")
                .child(email).child("trips");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        Trips trip = data.getValue(Trips.class);
                        tripDataList.add(trip);
                    }
                    adapter = new TripsAdapter(getContext(),R.layout.list_item,tripDataList);
                    linearLayout.setVisibility(View.INVISIBLE);
                    list.setAdapter(adapter);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((FoldingCell)view).toggle(false);
                adapter.registerToggle(i);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Do you want to delete");
                alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int l) {
                                tripDataList.remove(i);
                                TripsAdapter adapter = new TripsAdapter(getContext(), R.layout.list_item, tripDataList);
                                list.setAdapter(adapter);
                                final DatabaseReference myref = database.getReference().child("users").child(email).child("trips");
                                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int x = 0;
                                        for (DataSnapshot post : dataSnapshot.getChildren()) {
                                            if (x == i) {
                                                String key = post.getKey();
                                                myref.child(key).removeValue();
                                                break;
                                            }
                                            x++;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }}).setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int l) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new SaveTrip()).commit();
            }
        });
        return  view;
    }
}
