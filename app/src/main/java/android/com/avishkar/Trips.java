package android.com.avishkar;

public class Trips {
    public String source;
    public String destination;
    public String date;
    public int days;
    public int status;
    public String itirenrary;
    public double sLat;
    public double sLng;
    public double dLat;
    public double dLng;


    //1 for upcoming, 2 for past and 3 for current trips
    Trips()
    {

    }
    Trips(String source,String destination,String date,int days,String itirenrary,double sLat,
          double sLng,double dLat,double dLng,
          int status)
    {
        this.date=date;
        this.days=days;
        this.destination=destination;
        this.source=source;
        this.status=status;
        this.itirenrary = itirenrary;
        this.sLat = sLat;
        this.sLng = sLng;
        this.dLat = dLat;
        this.dLng = dLng;
    }
    public  double getsLat(){return  sLat;}
    public  double getsLng(){return  sLng;}
    public  double getdLat(){return  dLat;}
    public  double getdLng(){return  dLng;}
    public String getSource(){
        return source;
    }
    public String getDestination(){
        return destination;
    }
    public String getDate(){
        return date;
    }

    public int getDays(){
        return days;
    }
    public String getItirenrary(){return  itirenrary;}
    public int getStatus(){
        return status;
    }
}
