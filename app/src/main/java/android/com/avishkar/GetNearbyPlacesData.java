package android.com.avishkar;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    String url;
    public double latitud;
    public double longitud;
    public float color;
    GetNearbyPlacesData(double latitud,double longitud,int color)
    {
        this.latitud=latitud;
        this.longitud=longitud;
        switch(color){
            case 1:
                this.color =BitmapDescriptorFactory.HUE_RED;
                break;
            case 2:
                this.color =BitmapDescriptorFactory.HUE_YELLOW;
                break;
            case 3:
                this.color =BitmapDescriptorFactory.HUE_GREEN;
                break;
            case 4:
                this.color =BitmapDescriptorFactory.HUE_ORANGE;
                break;

        }
    }
    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.e("GetNearbyPlacesData", "doInBackground entered");
            MyMapLocation.map = (GoogleMap) params[0];
            Log.e("Map",MyMapLocation.map+"");
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser(latitud,longitud);
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String name = googlePlace.get("name");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(name);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
          //  Log.e("Map",MyMapLocation.map+"");
            MyMapLocation.map.addMarker(markerOptions);

        }
    }
}