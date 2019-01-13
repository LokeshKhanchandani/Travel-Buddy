package android.com.avishkar;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class PresentTrip extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public DaysAdapter mAdapter;
    public ArrayList<Days> mDays;
    Toolbar mToolbar;
    public String title, from, to, days, budget, email;
    Date startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_trip);

        final Context context = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mDays=new ArrayList<>();
        final TextView tit, source, dest, bud, tripdays, sdate;
        tit = (TextView) findViewById(R.id.title);
        source = (TextView) findViewById(R.id.source);
        dest = (TextView) findViewById(R.id.destination);
        bud = (TextView) findViewById(R.id.budget);
        tripdays = (TextView) findViewById(R.id.maxDays);
        sdate = (TextView) findViewById(R.id.startdate);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        final CurrentTour currtour = (CurrentTour) getIntent().getSerializableExtra("topresenttrip");
        email = getIntent().getExtras().getString("email");
        title = currtour.title.trim();
        from = currtour.source.trim();
        to = "To: " + currtour.destination.trim();
        days = currtour.tripDuration + "";
        budget = "Budget: " + currtour.budget;
        startDate = currtour.startDate;
        String formatedDate = (String) DateFormat.format("dd", startDate) + "," +
                (String) DateFormat.format("MMM", startDate) + "," +
                (String) DateFormat.format("yyyy", startDate);
        tit.setText(title);
        source.setText(from);
        dest.setText(to);
        bud.setText(budget);
        tripdays.setText(days);
        sdate.setText(formatedDate + "");
        mDays = currtour.days;
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addDay);
        Boolean state = currtour.state;
        if (state == true) {
            add.setClickable(false);
            add.setVisibility(View.INVISIBLE);
        }
        if (mDays != null) {
            mAdapter = new DaysAdapter(context, mDays, email);
            mRecyclerView.setAdapter(mAdapter);


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Days day = new Days();
                    mDays.add(day);
                    mAdapter = new DaysAdapter(context, mDays, email);
                    mRecyclerView.setAdapter(mAdapter);
                }
            });
            Button addItems = (Button) mToolbar.findViewById(R.id.itirenary_list);
//            FloatingActionButton addItem = (FloatingActionButton)findViewById(R.id.addItems);
            addItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itineraryIntent = new Intent(PresentTrip.this, ItineraryActivity.class);
                    itineraryIntent.putExtra("email", email);
                    startActivity(itineraryIntent);
                }
            });
            Button sharing = (Button) mToolbar.findViewById(R.id.share);
//            FloatingActionButton share = (FloatingActionButton)findViewById(R.id.share);
            sharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Hey, checkout my ongoing trip to ";
                    shareBody += currtour.destination.trim();
                    shareBody += " from ";
                    shareBody += from + "\n";
                    if (mDays != null) {
                        for (int i = 0; i < mDays.size(); i++) {
                            shareBody += "Day " + (i + 1) + ":\n\t";
                            shareBody += "How was my day: " + mDays.get(i).description + "\n\t";
                            shareBody += "Expenses: " + mDays.get(i).expenses + "\t\n";
                        }
                    }
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });
            Button publish = (Button) mToolbar.findViewById(R.id.publish);
            if (currtour.state == false) {
                publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference myref = firebaseDatabase.getReference().child("users").child(email).child("ongoingTrip").child("current");
                        myref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                CurrentTour currentTour = dataSnapshot.getValue(CurrentTour.class);
                                currentTour.state = true;
                                myref.removeValue();
                                firebaseDatabase.getReference().child("users").child(email).child("ongoingTrip").child("past").push().setValue(currentTour);
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
            else
            {
                publish.setVisibility(View.INVISIBLE);
                publish.setClickable(false);
            }
            }
        }
    }