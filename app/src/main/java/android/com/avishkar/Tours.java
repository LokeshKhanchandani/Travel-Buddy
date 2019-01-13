package android.com.avishkar;

import java.util.ArrayList;

public class Tours {
    public double longitude,latitude;
    public Boolean open;
    public String place_id;
    public String address;
    public String url;
    public String nam;
    public String city,type,description;
    public double temperature,humidity,wind;


    public double rating;
    public String types;
    Tours(String place_id, String address, String url, String nam, double rating, String types, Boolean open)
    {
        this.address=address;
        this.nam=nam;
        this.place_id=place_id;
        this.rating=rating;
        if(this.rating==0)
            this.rating=3.5;
        this.open=open;
        this.types=types;
        this.url=url;
    }
    Tours(String city,String type,String description,double temperature,double humidity,double wind){
        this.city = city;
        this.type = type;
        this.temperature = temperature;
        this.description  = description;
        this.humidity = humidity;
        this.wind = wind;
    }

    Tours(String city,String address,double lat,double lon) {
        this.city = city;
        this.address = address;
        this.latitude = lat;
        this.longitude = lon;
        this.open=true;
        this.temperature=0;
        this.description="";

    }
}
