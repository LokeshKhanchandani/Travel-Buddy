package android.com.avishkar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private ImageView mImageView;
    String username,originalemail,mobile;
    TextView em,mo;
    EditText nam;
    boolean x;

    StorageReference mStorageRef;
    FirebaseDatabase database;
    DatabaseReference picRef;
    StorageTask mUploadTask;
    String email;
    String mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mImageView=(ImageView)findViewById(R.id.profilepic);

        em=(TextView)findViewById(R.id.email);
        nam=(EditText) findViewById(R.id.name);
        mo=(TextView)findViewById(R.id.mobile);
        email=getIntent().getExtras().getString("email");
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads").child(email);
        picRef=database.getReference().child("users").child(email).child("profile/uri");
        DatabaseReference myref=database.getReference().child("users").child(email).child("profile");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserLogin user=dataSnapshot.getValue(UserLogin.class);
                originalemail=user.email;
                username=user.name;
                mobile=user.mob;
                mUri=user.uri;
                em.setText(originalemail);
                nam.setText(username);
                nam.setFocusable(false);
                nam.setFocusableInTouchMode(false);
                nam.setClickable(false);
                mo.setText(mobile);
                if(!mUri.equals("")){
                    Picasso.with(Profile.this).load(mUri)
                            .fit().centerCrop().into(mImageView);
                }
//                Toast.makeText(Profile.this,"Data "+originalemail,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ImageView edit=(ImageView)findViewById(R.id.edit);
        x=false;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!x){
                    edit.setImageResource(R.drawable.ic_subdirectory_arrow_right_black_24dp);
                    nam.setFocusable(true);
                    nam.setFocusableInTouchMode(true);
                    nam.setClickable(true);
                    x=true;
                }
                else{
                    username=nam.getText().toString();
                    edit.setImageResource(R.drawable.ic_edit_black_24dp);
                    nam.setFocusable(false);
                    nam.setFocusableInTouchMode(false);
                    nam.setClickable(false);
                    x=false;
                    FirebaseDatabase database2=FirebaseDatabase.getInstance();
                    DatabaseReference refe=database2.getReference().child("users").child(email).child("profile/name");
                    refe.setValue(username);
                }
            }
        });
    }

    public void changeImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri)
                    .fit().centerCrop().into(mImageView);
            uploadFile();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        if(mImageUri!=null){
            StorageReference fileReference=mStorageRef.child("profilepic"+getFileExtension(mImageUri));
            mUploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Profile.this, "Upload successful", Toast.LENGTH_LONG).show();
                            String uploadID=taskSnapshot.getDownloadUrl().toString();
                            picRef.setValue(uploadID);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
