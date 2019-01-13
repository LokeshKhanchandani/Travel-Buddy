package android.com.avishkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Temp extends AppCompatActivity {

    private EditText titlet,fromt,tot,dayst,costt;
    private String titleS,fromS,toS,daysS,costS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Button saveDetails=(Button)findViewById(R.id.presentTourDetails);
        final String email=getIntent().getExtras().getString("email");
        Toast.makeText(Temp.this,email,Toast.LENGTH_LONG).show();
        titlet=(EditText)findViewById(R.id.title_temp);
        fromt=(EditText)findViewById(R.id.from_temp);
        tot=(EditText)findViewById(R.id.to_temp);
        costt=(EditText)findViewById(R.id.cost_temp);
        dayst=(EditText)findViewById(R.id.days_temp);
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Days>days1=new ArrayList<>();
                titleS=titlet.getText().toString();
                fromS=fromt.getText().toString();
                toS=tot.getText().toString();
                daysS=dayst.getText().toString();
                costS=costt.getText().toString();

                //CurrentTour currentTour=new CurrentTour("trip","Allahabad","Banaras",false,days1,10,5000);
                CurrentTour currentTour=new CurrentTour(titleS,fromS,toS,false,days1,daysS,costS,"");
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference();
                myRef.child("users").child(email).child("ongoingTrip").child("current").setValue(currentTour);
                Intent temp=new Intent(Temp.this,PresentTrip.class);
                temp.putExtra("email",email);
                temp.putExtra("topresenttrip",currentTour);
                startActivity(temp);
                finish();
            }
        });
    }
}
