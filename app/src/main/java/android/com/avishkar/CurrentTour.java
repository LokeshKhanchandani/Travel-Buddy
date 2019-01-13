package android.com.avishkar;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lokesh on 18/9/18.
 */

public class CurrentTour implements Serializable {
    public Date startDate;
    public String title,source,destination;
    public boolean state;
    public String tripDuration,budget;
    public String itirenary;
    public ArrayList<Days> days;

    CurrentTour(){
        startDate=new Date();
        title=destination=source="";
        tripDuration=budget="";
        state=false;
        days=new ArrayList<>();
    }
    CurrentTour(String title,String source,String destination,boolean state,ArrayList<Days>days1,
                String tripDuration,String budget,String itirenary){
        this.title=title;
        this.source=source;
        this.destination=destination;
        this.state=state;
        this.days=days1;
        this.tripDuration=tripDuration;
        this.budget=budget;
        this.startDate=new Date();
        this.itirenary = itirenary;
    }
}