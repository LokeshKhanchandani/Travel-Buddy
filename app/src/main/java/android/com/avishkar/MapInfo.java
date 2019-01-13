package android.com.avishkar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapInfo extends Fragment {
    JSONObject weather;
    public String city;
    public String cityAdd;
    public double latitude;
    public double longitude;
    public TextView cityView;
    public TextView addView;
    public TextView latView;
    public TextView lonView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.map_info, container, false);
        Tours tour = ((Home) getActivity()).getTours();
        city = tour.city;
        cityAdd = tour.address;
        latitude = tour.latitude;
        longitude = tour.longitude;
        cityView = view.findViewById(R.id.place);
        addView = view.findViewById(R.id.address);
        latView = view.findViewById(R.id.lat);
        lonView = view.findViewById(R.id.lon);
        return view;
    }

}
