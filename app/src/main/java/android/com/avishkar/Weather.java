package android.com.avishkar;

/**
 * Created by lokesh on 1/9/18.
 */

class Weather{
    public String city;
    public String type;
    public String description;
    public double temperature;
    public double humidity;
    public double wind;

    Weather(String city,String type,String description,double temperature,double humidity,double wind){
        this.city = city;
        this.type = type;
        this.temperature = temperature;
        this.description  = description;
        this.humidity = humidity;
        this.wind = wind;
    }

}
