package android.com.avishkar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by lokesh on 1/7/18.
 */
@NonReusable
@Layout(R.layout.drawer_header)
public class Drawerheader {




    @View(R.id.weather)
    private Button weather;

    private String mname,memail;
    private LatLng mLatLng;
    Context mContext;

    public Drawerheader(Context context, LatLng latLng)
    {
        mContext=context;
        mLatLng = latLng;
    }

    @Resolve
    private void onResolved() {
//        nameTxt.setText(mname);
//        emailTxt.setText(memail);
        weather.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Toast.makeText(mContext,"Weather",Toast.LENGTH_LONG).show();
                Log.e("Weather",mContext.toString());


                Intent featuresIntent = new Intent(mContext,Features.class);
                if(mLatLng==null){
                    mLatLng = new LatLng(25.4918881,81.86750959);
                }
                double lat = mLatLng.latitude;
                double lon = mLatLng.longitude;
                featuresIntent.putExtra("latitude",lat);
                featuresIntent.putExtra("longitude",lon);
                featuresIntent.putExtra("feature",0);
                featuresIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(featuresIntent);
            }
        });
    }
}
