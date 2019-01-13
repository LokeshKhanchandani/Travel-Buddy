package android.com.avishkar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Features extends AppCompatActivity {
    public double latitude;
    MyReceiver myReceiver;
    public double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);


        Intent returnIntent = getIntent();
        int returnFeatures = returnIntent.getExtras().getInt("feature");
        Log.e("Code", returnFeatures + "");

        switch (returnFeatures) {
            case 0:
                latitude = returnIntent.getExtras().getDouble("latitude");
                longitude = returnIntent.getExtras().getDouble("longitude");
                Toast.makeText(this, "ServiceStarted", Toast.LENGTH_LONG).show();
                Intent weatherServiceIntent = new Intent(this, WeatherService.class);
                weatherServiceIntent.putExtra("latitude", latitude);
                weatherServiceIntent.putExtra("longitude", longitude);
                weatherServiceIntent.putExtra("type", 0);
                startService(weatherServiceIntent);
                break;
            case 1:
                String city = returnIntent.getExtras().getString("city");
                Log.e("Feature", city);
                Intent travelServiceIntent = new Intent(this, WeatherService.class);
                travelServiceIntent.putExtra("city", city);
                travelServiceIntent.putExtra("type", 1);
                travelServiceIntent.putExtra("place", "museum");
                startService(travelServiceIntent);
                break;
            case 2:
                String tcity = returnIntent.getExtras().getString("city");
                Log.e("Feature1", tcity);
                Intent transServiceIntent = new Intent(this, WeatherService.class);
                transServiceIntent.putExtra("city", tcity);
                transServiceIntent.putExtra("type", 2);
                transServiceIntent.putExtra("place", "museum");
                startService(transServiceIntent);
                break;
            case 3:
                String tcity2 = returnIntent.getExtras().getString("city");
                Log.e("Feature1", tcity2);
                Intent railServiceIntent = new Intent(this, WeatherService.class);
                railServiceIntent.putExtra("city", tcity2);
                railServiceIntent.putExtra("type", 3);
                railServiceIntent.putExtra("place", "museum");
                startService(railServiceIntent);
                break;
            case 4:
                String tcity3 = returnIntent.getExtras().getString("city");
                Log.e("Feature1", tcity3);
                Intent busServiceIntent = new Intent(this, WeatherService.class);
                busServiceIntent.putExtra("city", tcity3);
                busServiceIntent.putExtra("type", 4);
                busServiceIntent.putExtra("place", "museum");
                startService(busServiceIntent);
                break;
            case 5:
                String tcity4 = returnIntent.getExtras().getString("city");
                Log.e("Feature1", tcity4);
                Intent restServiceIntent = new Intent(this, WeatherService.class);
                restServiceIntent.putExtra("city", tcity4);
                restServiceIntent.putExtra("type", 5);
                restServiceIntent.putExtra("place", "museum");
                startService(restServiceIntent);
                break;
            case 6:
                String tcity5 = returnIntent.getExtras().getString("city");
                Log.e("Feature1", tcity5);
                Intent cafeServiceIntent = new Intent(this, WeatherService.class);
                cafeServiceIntent.putExtra("city", tcity5);
                cafeServiceIntent.putExtra("type", 6);
                cafeServiceIntent.putExtra("place", "museum");
                startService(cafeServiceIntent);
                break;
            case 7:
                String tcity6 = returnIntent.getExtras().getString("city");
                Log.e("Feature1", tcity6);
                Intent hospitalServiceIntent = new Intent(this, WeatherService.class);
                hospitalServiceIntent.putExtra("city", tcity6);
                hospitalServiceIntent.putExtra("type", 7);
                hospitalServiceIntent.putExtra("place", "museum");
                startService(hospitalServiceIntent);
                break;
            case 8:
                String tcity7 = returnIntent.getExtras().getString("city");
                Log.e("Feature1", tcity7);
                Intent atmServiceIntent = new Intent(this, WeatherService.class);
                atmServiceIntent.putExtra("city", tcity7);
                atmServiceIntent.putExtra("type", 8);
                atmServiceIntent.putExtra("place", "museum");
                startService(atmServiceIntent);
                break;
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (arg1.getExtras().getInt("type")) {
                case 0:
                    String description = arg1.getExtras().getString("description");
                    String temperature = arg1.getExtras().getString("temperature");
                    String humidity = arg1.getExtras().getString("humidity");
                    String w_speed = arg1.getExtras().getString("w_speed");
                    String country = arg1.getExtras().getString("country");
                    String city = arg1.getExtras().getString("city");
                    Log.e("Feature", description);
                    TextView weatherdesc,weathertemp,weatherhumi,weatherwind,weathercity;
                    weatherdesc=(TextView)findViewById(R.id.description);
                    weathertemp=(TextView)findViewById(R.id.temreture);
                    weatherhumi=(TextView)findViewById(R.id.humidity);
                    weatherwind=(TextView)findViewById(R.id.windspeed);
                    weathercity=(TextView)findViewById(R.id.feature);
                    LinearLayout card=(LinearLayout)findViewById(R.id.setimage);
                    CardView cardback=(CardView)findViewById(R.id.weather_card_item);
                    weatherdesc.setText(description);
                    weathertemp.setText(temperature+"°C");
                    weatherhumi.setText(humidity+"%");
                    weatherwind.setText(w_speed+"km/hr");
                    weathercity.setText(city+"'s Weather");
                    if(description.contains("sun")||description.contains("clear")){
                        card.setBackgroundResource(R.drawable.sun);
                        cardback.setCardBackgroundColor(getResources().getColor(R.color.sunny));
                    }
                    else if(description.contains("cloud")){
                        card.setBackgroundResource(R.drawable.cloudy);
                        cardback.setCardBackgroundColor(getResources().getColor(R.color.cloudy));
                    }
                    else {
                        card.setBackgroundResource(R.drawable.rainy);
                        cardback.setCardBackgroundColor(getResources().getColor(R.color.rainy));
                    }
                    break;
                case 1:
                    // RECIVE TRAVEL on switch 1
                    Bundle bund=arg1.getBundleExtra("bundle");
                    ArrayList<Travel> travels = (ArrayList<Travel>) bund.getSerializable("list");
                    if(travels!=null) {
                        String name = travels.get(0).form_add;
                        Toast.makeText(Features.this, name, Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
            }
        }

    }
}
