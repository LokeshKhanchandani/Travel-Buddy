package android.com.avishkar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.com.avishkar.MapsActivity.drawMarker;
import static java.lang.Thread.sleep;

/**
 * Created by UTSAV JAIN on 8/30/2018.
 */

public class MyMapLocation extends FragmentActivity implements OnMapReadyCallback {
    private static final int PLACE_PICKER_REQUEST = 1;
    public Marker marker;
    public static GoogleMap map;
    double lat = 0, lng = 0;
    double latitude = 25.495941, longitude = 81.8631611;
    String city;
    double sLat,sLng,dLat,dLng;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;
    private Toolbar mToolbar;
    public int feature;
    private Button searchView;
    public Intent in;
    public boolean flag = false;
    View view;
    LocationManager locationManager;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_map_location);
        view = findViewById(R.id.rootView);
        in = getIntent();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Route");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        Button recenter = mToolbar.findViewById(R.id.recenter);
        recenter.setClickable(false);
        recenter.setVisibility(View.INVISIBLE);
        feature = getIntent().getExtras().getInt("feature");
        searchView = mToolbar.findViewById(R.id.search_city);
        if(feature!=101&&feature!=102) {
            latitude = getIntent().getExtras().getDouble("lat");
            longitude = getIntent().getExtras().getDouble("lng");
            mToolbar.setTitle("Search");
            searchView.setClickable(false);
            searchView.setVisibility(View.INVISIBLE);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MyMapLocation.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
//        Intent intent = null;
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//             intent = builder.build(this);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                flag = true;
                map.clear();
                drawMarker(new LatLng(latitude, longitude));
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng query = place.getLatLng();
                lat = query.latitude;
                lng = query.longitude;
                drawMarker(new LatLng(lat, lng));
                city = place.getName().toString();
                String url =
                        "https://maps.googleapis.com/maps/api/directions/json?origin="
                                + latitude + "," + longitude + "&destination="
                                + lat + "," + lng + "&sensor=false&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                makeLoacation(url);
                Object[] DataTransfer = new Object[2];
                String url1 = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+tourists+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                GetNearbyPlacesData getNearbyPlacesData;
                DataTransfer[0]=map;
                DataTransfer[1] = url1;
                getNearbyPlacesData = new GetNearbyPlacesData(lat, lng, 1);
                getNearbyPlacesData.execute(DataTransfer);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataTransfer[0]=map;
                url1="https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+railways+airports+bus+stations+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url1;
                getNearbyPlacesData = new GetNearbyPlacesData(lat, lng, 2);
                getNearbyPlacesData.execute(DataTransfer);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataTransfer[0]=map;
                url1="https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+cafes+restaurants+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url1;
                getNearbyPlacesData = new GetNearbyPlacesData(lat, lng, 3);
                getNearbyPlacesData.execute(DataTransfer);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataTransfer[0]=map;
                url1="https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+hospitals+atm+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url1;
                getNearbyPlacesData = new GetNearbyPlacesData(lat, lng, 4);
                getNearbyPlacesData.execute(DataTransfer);

            }

        }
    }

    public void drawMarker(LatLng point) {
        // Creating an instance of MarkerOptions

        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker+--
        markerOptions.position(point);
        String ad = null;

        marker = map.addMarker(markerOptions);
        Geocoder geocoder = new Geocoder(MyMapLocation.this, Locale.getDefault());
        try {
            Log.e("loglat", point.latitude + " " + point.longitude);
            List<Address> adress = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            Log.e("adress", adress + "");
            ad = adress.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        int c = 0;
        for (int i = 0; i < ad.length(); i++) {
            if (ad.charAt(i) == ',' && c == 0) {
                c++;
                continue;
            } else if (ad.charAt(i) == ',') {
                city = ad.substring(0, i);
                break;
            }
        }
        marker = map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16.0f));
        CameraPosition cameraPosition = CameraPosition.builder().target(point).zoom(10).bearing(0).tilt(75).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker.setTitle(city);
    }

    public JSONObject makeLoacation(String url) {
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
        System.out.println(returnValue);
        try {
            result = new JSONObject(returnValue);
            makePolyLine(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void makePolyLine(JSONObject result) {
        try {
            JSONArray routes = result.getJSONArray("routes");

            long distanceForSegment = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");
            Snackbar.make(view,"Distance: "+distanceForSegment/1000+" km",Snackbar.LENGTH_LONG).show();
            Log.e("Distance",distanceForSegment+"");
            JSONArray steps = routes.getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONArray("steps");
            List<LatLng> lines = new ArrayList<LatLng>();
            for (int i = 0; i < steps.length(); i++) {
                String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

                for (LatLng p : decodePolyline(polyline)) {
                    lines.add(p);
                }
            }
            map.addPolyline(new PolylineOptions().addAll(lines).width(10).color(Color.RED));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }

        return poly;
    }

    private String buildStringIOutils(InputStream is) {
        try {
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(feature!=101&&feature!=102) {
            drawMarker(new LatLng(latitude, longitude));
            city = in.getExtras().getString("city");
            searchView.setVisibility(View.INVISIBLE);
            searchView.setClickable(false);
        }
        Object[] DataTransfer;
        String url = "";
        GetNearbyPlacesData getNearbyPlacesData;
        switch (feature) {
            case 1:
                DataTransfer = new Object[2];
                DataTransfer[0] = map;
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+tourists+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 1);
                getNearbyPlacesData.execute(DataTransfer);
                break;
            case 2:
                DataTransfer = new Object[2];
                DataTransfer[0] = map;
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+railways+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 2);
                getNearbyPlacesData.execute(DataTransfer);
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+bus+statios+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 2);
                getNearbyPlacesData.execute(DataTransfer);
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+airports+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 2);
                getNearbyPlacesData.execute(DataTransfer);
                break;
            case 3:
                DataTransfer = new Object[2];
                DataTransfer[0] = map;
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+cafes+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 3);
                getNearbyPlacesData.execute(DataTransfer);
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+restaurants+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 3);
                getNearbyPlacesData.execute(DataTransfer);
                break;
            case 4:
                DataTransfer = new Object[2];
                DataTransfer[0] = map;
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+hospitals+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 4);
                getNearbyPlacesData.execute(DataTransfer);
                url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city + "+atm+interest&fields=geometry,name&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                DataTransfer[1] = url;
                getNearbyPlacesData = new GetNearbyPlacesData(latitude, longitude, 4);
                getNearbyPlacesData.execute(DataTransfer);
                break;
            case 101:
                View tool = findViewById(R.id.toolbar);
                searchView = mToolbar.findViewById(R.id.search_city);
                searchView.setVisibility(View.INVISIBLE);
                tool.findViewById(R.id.recenter).setVisibility(View.INVISIBLE);
                sLat = getIntent().getExtras().getDouble("source_latitude");
                sLng = getIntent().getExtras().getDouble("source_longitude");
                dLat = getIntent().getExtras().getDouble("destination_latitude");
                dLng = getIntent().getExtras().getDouble("destination_longitude");
                Log.e("Destination",dLat+" "+dLng);
                drawMarker(new LatLng(sLat,sLng));
                drawMarker(new LatLng(dLat,dLng));
                String url1 =
                        "https://maps.googleapis.com/maps/api/directions/json?origin="
                                + sLat + "," + sLng + "&destination="
                                + dLat + "," + dLng + "&sensor=false&key=AIzaSyAw-6HfE34y-PeMhTZz44YutxkxwQReqno";
                makeLoacation(url1);
                break;
            case 102:
                latitude = getIntent().getExtras().getDouble("lat");
                longitude = getIntent().getExtras().getDouble("lng");
                drawMarker(new LatLng(latitude,longitude));
                mToolbar.setTitle("Search");
                break;
        }

    }


}

