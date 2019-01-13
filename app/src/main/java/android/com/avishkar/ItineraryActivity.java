package android.com.avishkar;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.impl.client.DefaultTargetAuthenticationHandler;

public class ItineraryActivity extends AppCompatActivity {
    private Button mDone;
    private EditText mItem;
    private String mEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);
        mDone = (Button) findViewById(R.id.done_item);
        mItem = (EditText) findViewById(R.id.enter);
        mEmail = getIntent().getExtras().getString("email");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference db = firebaseDatabase.getReference().child("users").child(mEmail).
                child("ongoingTrip").child("current").child("itirenary");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String itirenary = dataSnapshot.getValue().toString();
                mItem.setText(itirenary);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itinerary = mItem.getText().toString();
                FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference();
                myRef.child("users").child(mEmail).child("ongoingTrip").child("current").child("itirenary").setValue(itinerary);
                Log.e("Item",itinerary);

                finish();
            }
        });

    }
}