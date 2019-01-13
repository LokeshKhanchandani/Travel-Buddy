package android.com.avishkar;

import android.*;
import android.app.ActionBar;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.majeur.cling.Cling;
import com.majeur.cling.ClingManager;
import com.majeur.cling.ViewTarget;
import com.mindorks.placeholderview.PlaceHolderView;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements Serializable, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static transient GoogleMap mMap;
    public static Marker marker;
    static LocationManager locationManager;
    ActionBar actionBar;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;
    private String email;
    static String city;
    static String lala;
    public static Tours tour;
    int id, j;
    boolean done = false;
    double latitude = 25.4918881, longitude = 81.86750959;
    double homeLatitude = 25.4918881, homeLongitude = 81.86750959;
    public LatLng latLng;
    View fragment;
    public String name = "Allahabad";
    //Navigation Drawer
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;
    private Button gpsView;
    private boolean state = true;
    private TextView placeview;
    private TextView address;
    private TextView latView;
    private TextView lonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        email = getIntent().getExtras().getString("email");
        fragment = findViewById(R.id.map_in);
        id = getIntent().getExtras().getInt("id");
        placeview = fragment.findViewById(R.id.place);
        address = fragment.findViewById(R.id.address);
        latView = fragment.findViewById(R.id.lat);
        lonView = fragment.findViewById(R.id.lon);
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView) findViewById(R.id.drawerView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        gpsView = (Button) findViewById(R.id.recenter);
        mDrawer.openDrawer(GravityCompat.START);
        mToolbar.setTitle("Home");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        Button searchView = mToolbar.findViewById(R.id.search_city);
        mGalleryView = (PlaceHolderView) findViewById(R.id.galleryView);
        setupDrawer();
        if (id==0)
        showTut();
        if (id==1) {
            FirebaseDatabase myfire = FirebaseDatabase.getInstance();
            final int[] new_user = {0};
            DatabaseReference myref = myfire.getReference().child("users").child(email);
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild("ongoingTrip")&&!dataSnapshot.hasChild("trips"))
                        new_user[0] = 1;
                    if (new_user[0] == 1)
                        showTut();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(Home.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Home.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0, locationListenerGPS);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                locationListenerGPS);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        gpsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
                try {
                    List<Address> adress = geocoder.getFromLocation(homeLatitude, homeLongitude, 1);
                    name = adress.get(0).getLocality();
                    String add = adress.get(0).getAddressLine(0);
                    mMap.clear();
                    placeview.setText(name);
                    address.setText(add);
                    latView.setText((int) homeLatitude + "°N");
                    lonView.setText((int) homeLongitude + "°S");
                    latitude = homeLatitude;
                    longitude = homeLongitude;
                    latLng = new LatLng(latitude, longitude);
                    setupDrawer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                drawMarker(new LatLng(homeLatitude, homeLongitude));
            }
        });


    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (state == false) {
                state = true;
                homeLatitude = location.getLatitude();
                homeLongitude = location.getLongitude();
            }


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                name = place.getName().toString();
                latLng = place.getLatLng();
                int lat, lng;
                lat = (int) latLng.latitude;
                lng = (int) latLng.longitude;
                mMap.clear();
                drawMarker(latLng);

                placeview.setText(name);

                address.setText(place.getAddress());

                latView.setText("Latitude " + String.valueOf(lat) + "°N");

                lonView.setText("Longitude " + String.valueOf(lng) + "°S");
                setupDrawer();
                Log.d("Name", name + "\n" + lat + "\n" + lng);
            }

        }
    }

    public void getdetails(LatLng latLng) {
        Toast.makeText(Home.this, "service", Toast.LENGTH_LONG).show();
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> adress = geocoder.getFromLocation(latitude, longitude, 1);
            city = adress.get(0).getLocality().toLowerCase();
            lala = "";
            lala = lala + adress.get(0).getAddressLine(0);
            System.out.print(city);
            Toast.makeText(Home.this, city + lala + latitude + longitude, Toast.LENGTH_LONG).show();
            tour = new Tours(city, lala, latitude, longitude);
//            TextView place = fragment.findViewById(R.id.place);
//            place.setText(city);
//            TextView address = fragment.findViewById(R.id.address);
//            address.setText(lala);
//            TextView lat = fragment.findViewById(R.id.lat);
//            lat.setText(String.valueOf(latitude));
//            TextView lon = fragment.findViewById(R.id.lon);
//            lon.setText(String.valueOf(longitude));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city +
                "+Tourist&language=en&key=AIzaSyCLAkq9FBr_0tfE4HvRGpe_g7I5i8rXYTU";
        HttpResponse response = null;
        HttpGet request;
        JSONObject result = null;
        DefaultHttpClient client = new DefaultHttpClient();

        request = new HttpGet(url);
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream source = null;
        try {
            source = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String returnValue = buildStringIOutils(source);
        try {


            result = new JSONObject(returnValue);
            JSONArray tours = result.getJSONArray("results");


            for (int i = 0; i < tours.length(); i++) {
                JSONArray place = tours.getJSONObject(i).getJSONArray("types");
                System.out.println(place.length());
                for (j = 0; j < place.length(); j++) {
                    if (place.getString(j).equals("museum") || place.getString(j).equals("establishment")) {
                        break;
                    }
                }
                if (j != place.length() && Double.parseDouble(tours.getJSONObject(i).getString("rating")) >= 2.5) {
                    String placeid = tours.getJSONObject(i).getString("place_id");
                    String address = tours.getJSONObject(i).getString("formatted_address");
                    String link = tours.getJSONObject(i).getString("icon");
                    String name = tours.getJSONObject(i).getString("name");
                    double rating = Double.parseDouble(tours.getJSONObject(i).getString("rating"));
                    String types = "Tourism";
                    Tours tours1 = new Tours(placeid, address, link, name, rating, types, true);
                    //list.add(tours1);
                }
                //if (list.size()==3)
                //break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Tours tour=new Tours("Allahabad","weather","Rainy",37,99,550);
        //list.add(tour);
//            ArrayList<HashMap<String,String>> arrayList= new ArrayList<>();
//            if(list!=null) {
//                for (int i = 0; i < list.size(); i++) {
//                    HashMap<String, String> hm = new HashMap<>();
//                    hm.put("name", list.get(i).address);
//                    arrayList.add(hm);
//                }
//            }
        String[] s = {"name"};
        int[] t = {R.id.tv};
//            if (list!=null)
//            {
//                mAdapter=new CardAdapter(getApplicationContext(),list);
//                mRecyclerView.setAdapter(mAdapter);
//            }
    }

    public static String buildStringIOutils(InputStream is) {
        try {
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void drawMarker(final LatLng point) {
        // Creating an instance of MarkerOptions
        // Toast.makeText(Home.this,"drawn",Toast.LENGTH_LONG).show();
        MarkerOptions markerOptions = new MarkerOptions();
        latitude = point.latitude;
        longitude = point.longitude;
        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 10.0f));
        CameraPosition cameraPosition = CameraPosition.builder().target(point).zoom(17).bearing(0).tilt(75).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);


    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent placePickerIntent = new Intent(Home.this, MyMapLocation.class);
        placePickerIntent.putExtra("lat", latitude);
        placePickerIntent.putExtra("lng", longitude);
        placePickerIntent.putExtra("city", name);
        placePickerIntent.putExtra("feature",102);
        startActivity(placePickerIntent);
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlng1 = new LatLng(25.4918881, 81.86750959);
        drawMarker(latlng1);
        mMap.setOnMarkerClickListener(Home.this);
    }

    public Tours getTours() {
        if (tour != null)
            return tour;
        return new Tours("city", "ad", 0.00, 0.00);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawer() {
        //Toast.makeText(getApplicationContext(),user+"this"+cour,Toast.LENGTH_SHORT).show();
        mDrawerView.removeAllViews();
        mDrawerView
                .addView(new Drawerheader(this.getApplicationContext(), latLng));
        if (id == 1)
            mDrawerView.addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE, email, latitude, longitude))
                    .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HOME, email, latitude, longitude));
        mDrawerView.addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_TOURISTS, name, latitude, longitude))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_TRANSPORTS, name, latitude, longitude))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_ETERIES, name, latitude, longitude))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_AIDS, name, latitude, longitude));
        if (id == 1)
            mDrawerView.addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT, email, latitude, longitude))
                    ;

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }
    private void showTut() {
        ClingManager mClingManager = new ClingManager(this);

        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Welcome to Gallivanters")
                .setMessageBackground(getResources().getColor(R.color.profile))
                .setContentTextAppearance(R.id.card_item)
                .setContent("An application that will locate everything for you")
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Current Location")
                .setContent("Set  your current location")
                .setMessageBackground(getResources().getColor(R.color.profile))
                .setContentTextAppearance(R.id.card_item)
                .setTarget(new com.majeur.cling.ViewTarget(this, R.id.recenter))
                .build());
        mClingManager.addCling(new Cling.Builder(this)
                .setTitle("Checkout the Drawer")
                .setContent("Here you can check weather, tourist places and many more things !!")
                .setMessageBackground(getResources().getColor(R.color.profile))
                .setContentTextAppearance(R.id.card_item)
                .setTarget(new ViewTarget(this,R.id.toolbar))
                .build()
        );
        mClingManager.start();
    }

}
