package android.com.avishkar;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.support.v7.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity implements Serializable,OnMapReadyCallback,LocationListener, GoogleMap.OnMarkerClickListener {

    private static transient GoogleMap mMap;
    public static Marker marker;

    JSONObject weather;
    public String type;
    public String description;
    public double temperature;
    public double humidity;
    public double wind;
    private String email;
    private FloatingActionButton fButton;
    private static RecyclerView mRecyclerView;
    static private CardAdapter mAdapter;
    static ArrayList<Tours> list;
    static LocationManager locationManager;
    double latitude, longitude;
    static String city;
    static String lala;
    boolean done = false;
    static int j;
    int id;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        email=getIntent().getExtras().getString("email");
        id=getIntent().getExtras().getInt("id");
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0, locationListenerGPS);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                locationListenerGPS);


        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Nearby");

        Context context=this;
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(context,R.color.colorAccent));

        ImageView header=(ImageView)findViewById(R.id.header);
        Picasso.with(this).load(R.drawable.mnnit).fit().centerCrop().into(header);
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fButton = (FloatingActionButton) findViewById(R.id.float_button);
        if (id==0){
            fButton.setVisibility(View.INVISIBLE);
            fButton.setClickable(false);}
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dashboardIntent = new Intent(MapsActivity.this,Dashboard.class);
                dashboardIntent.putExtra("email",email);
                startActivity(dashboardIntent);
            }
        });
    }
    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (done==false) {
                Log.e("OnLoacation", "Changed");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                done = true;
                Intent intent = new Intent(MapsActivity.this, LocationService.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                startService(intent);
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


    public  static String buildStringIOutils(InputStream is) {
        try {
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    public  static void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions);


    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude= location.getLongitude();
        LatLng latLng = new LatLng(latitude,longitude);
        drawMarker(latLng);
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

    @Override
    public boolean onMarkerClick(Marker marker) {

            Intent placePickerIntent = new Intent(MapsActivity.this, MyMapLocation.class);
            placePickerIntent.putExtra("lat",latitude);
            placePickerIntent.putExtra("lng",longitude);
            startActivity(placePickerIntent);
            return false;


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(MapsActivity.this);
    }

    public void pareseWeather(JSONObject weather) throws JSONException {

        type = weather.getJSONArray("weather").getJSONObject(0).getString("main");
        description = weather.getJSONArray("weather").getJSONObject(0).getString("description");
        temperature = weather.getJSONObject("main").getDouble("temp");
        humidity = weather.getJSONObject("main").getDouble("humidity");
        wind = weather.getJSONObject("wind").getDouble("speed");
    }

    public void getWeather(final JSONObject city) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=ea574594b9d36ab688642d5fbeab847e");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    weather = new JSONObject(json.toString());

                    if(weather.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }


                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(weather!=null){
                    Log.d("my weather received",weather.toString());
                }

            }
        }.execute();

    }
    @Keep
    public static  class LocationService extends Service {
        public LocationService() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.e("In Service","Service");
            double latitude = intent.getExtras().getDouble("Latitude");
            double longitude = intent.getExtras().getDouble("Longitude");
            list = new ArrayList<>();
            mMap.clear();
            drawMarker(new LatLng(latitude,longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16.0f));
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address>adress=geocoder.getFromLocation(latitude,longitude,1);
                city=adress.get(0).getLocality().toLowerCase();
                lala="";
                lala=lala+adress.get(0).getAddressLine(0);
                System.out.print(city);
                Log.d("current",city+lala+latitude+longitude);
                Tours tour=new Tours(city,lala,latitude,longitude);
                list.add(tour);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String url="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+city+
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


                for (int i=0;i<tours.length();i++)
                {
                    JSONArray place = tours.getJSONObject(i).getJSONArray("types");
                    System.out.println(place.length());
                    for ( j=0;j<place.length();j++)
                    {
                        if (place.getString(j).equals("museum") || place.getString(j).equals("establishment"))
                        {
                            break;
                        }
                    }
                    if (j!=place.length()&&Double.parseDouble(tours.getJSONObject(i).getString("rating"))>=2.5){
                        String placeid= tours.getJSONObject(i).getString("place_id");
                        String address=tours.getJSONObject(i).getString("formatted_address");
                        String link=tours.getJSONObject(i).getString("icon");
                        String name=tours.getJSONObject(i).getString("name");
                        double rating = Double.parseDouble(tours.getJSONObject(i).getString("rating"));
                        String types="Tourism";
                        Tours tours1 =new Tours(placeid,address,link,name,rating,types,true);
                        list.add(tours1);}
                    if (list.size()==3)
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            /// WEATHER PART
//                getWeather(weather);
////                city = "Allahabad";
//                try {
//                    Log.d("weather",weather+"");
//                    pareseWeather(weather);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            Tours tour=new Tours("Allahabad","weather","Rainy",37,99,550);
            list.add(tour);
            ArrayList<HashMap<String,String>> arrayList= new ArrayList<>();
            if(list!=null) {
                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("name", list.get(i).address);
                    arrayList.add(hm);
                }
            }
            String[]  s={"name"};
            int [] t={R.id.tv};
            if (list!=null)
            {
                mAdapter=new CardAdapter(getApplicationContext(),list);
                mRecyclerView.setAdapter(mAdapter);
            }
            return START_STICKY;
        }


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }



}

