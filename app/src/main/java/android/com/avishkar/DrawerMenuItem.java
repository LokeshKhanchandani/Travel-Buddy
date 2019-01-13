package android.com.avishkar;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

//import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by lokesh on 1/7/18.
 */
@Layout(R.layout.drawer_item)


public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_PROFILE = 1;
    public static final int DRAWER_MENU_ITEM_HOME = 2;
    public static final int DRAWER_MENU_ITEM_TOURISTS = 3;
    public static final int DRAWER_MENU_ITEM_TRANSPORTS = 4;
    public static final int DRAWER_MENU_ITEM_ETERIES = 5;
    public static final int DRAWER_MENU_ITEM_AIDS = 6;
    public static final int DRAWER_MENU_ITEM_LOGOUT = 7;


    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;
    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;

    @View(R.id.drawerLayout)
    private DrawerLayout drawer;

    private String memail;
    private String mcity;
    private double mLatitude,mLongitude;

    public DrawerMenuItem(Context context, int menuPosition, String city,double latitude,double longitude ) {
        if(city==null)
            city="Allahabad";
        mLatitude = latitude;
        mLongitude = longitude;
        mcity = city;
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_PROFILE:
                itemNameTxt.setText("Profile");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_account_box_black_24dp));
                break;
            case DRAWER_MENU_ITEM_HOME:
                itemNameTxt.setText("My Trips");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_black_24dp));
                break;
            case DRAWER_MENU_ITEM_TOURISTS:
                itemNameTxt.setText("Tourist Places");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_tourist_24dp));
                break;
            case DRAWER_MENU_ITEM_TRANSPORTS:
                itemNameTxt.setText("Transport");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_directions_bus_black_24dp));
                break;
            case DRAWER_MENU_ITEM_ETERIES:
                itemNameTxt.setText("Eteries");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_restaurant_24dp));
                break;
            case DRAWER_MENU_ITEM_AIDS:
                itemNameTxt.setText("Aids");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_local_hospital_black_24dp));
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_input_black_24dp));
                itemNameTxt.setText("Logout");
                break;

        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_PROFILE:
                Intent profileIntent = new Intent(mContext, Profile.class);
                profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                profileIntent.putExtra("email",mcity);
                mContext.startActivity(profileIntent);
                if (mCallBack != null) mCallBack.onProfileMenuSelected();
                break;
            case DRAWER_MENU_ITEM_TOURISTS:
                Toast.makeText(mContext, "Tourists", Toast.LENGTH_SHORT).show();
                Intent inten = new Intent(mContext, MyMapLocation.class);
                Log.e("DrawerMenu", String.valueOf(mcity));
                inten.putExtra("city", mcity);
                inten.putExtra("lat",mLatitude);
                inten.putExtra("lng",mLongitude);
                inten.putExtra("feature", 1);

                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(inten);
                if (mCallBack != null) mCallBack.onTravelMenuSelected();
                break;
            case DRAWER_MENU_ITEM_TRANSPORTS:
                Toast.makeText(mContext,"Transport",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,MyMapLocation.class);
                intent.putExtra("city",mcity);
                intent.putExtra("lat",mLatitude);
                intent.putExtra("lng",mLongitude);
                intent.putExtra("feature",2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                if (mCallBack != null) mCallBack.onPROJECTSMenuSelected();
                break;
            case DRAWER_MENU_ITEM_ETERIES:
                Toast.makeText(mContext,"Eteries",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(mContext,MyMapLocation.class);
                intent2.putExtra("city",mcity);
                intent2.putExtra("lat",mLatitude);
                intent2.putExtra("lng",mLongitude);
                intent2.putExtra("feature",3);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent2);
                if (mCallBack != null) mCallBack.onSHAREMenuSelected();
                break;
            case DRAWER_MENU_ITEM_AIDS:
                Toast.makeText(mContext, "Aids", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(mContext,MyMapLocation.class);
                intent3.putExtra("city",mcity);
                intent3.putExtra("lat",mLatitude);
                intent3.putExtra("lng",mLongitude);
                intent3.putExtra("feature",4);
                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent3);
                if (mCallBack != null) mCallBack.onNotificationsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(mContext,Main2Activity.class);
                mContext.startActivity(intent1);
                System.exit(0);
                if (mCallBack != null) mCallBack.onSettingsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_HOME:
                Intent intent4 = new Intent(mContext,Dashboard.class);
                intent4.putExtra("email",mcity);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent4);
                if (mCallBack != null) mCallBack.onSettingsMenuSelected();
                break;


        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack {
        void onProfileMenuSelected();

        void onTravelMenuSelected();

        void onPROJECTSMenuSelected();

        void onSHAREMenuSelected();

        void onNotificationsMenuSelected();

        void onSettingsMenuSelected();


    }
}
