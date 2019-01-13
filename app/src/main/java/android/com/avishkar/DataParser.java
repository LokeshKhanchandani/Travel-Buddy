package android.com.avishkar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private double lat;
    public double lng;
    DataParser(double lat,double lng)
    {
        this.lat=lat;
        this.lng=lng;
    }
    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < min(5,placesCount); i++) {
             try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private int min(int i, int placesCount) {
        if (i<=placesCount)
            return i;
        return placesCount;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String name="--N/A--";
        String lat = "0.00";
        String lng = "0.00";
        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                name = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("geometry"))
            {
                lat = String.valueOf(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                lng = String.valueOf(googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            }
            googlePlaceMap.put("name", name);
            googlePlaceMap.put("lat",lat);
            googlePlaceMap.put("lng",lng);
            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}



