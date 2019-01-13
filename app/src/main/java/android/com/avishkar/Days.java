package android.com.avishkar;

import java.io.Serializable;

import static com.google.android.gms.internal.zzbgp.NULL;

/**
 * Created by lokesh on 18/9/18.
 */

public class Days implements Serializable{
    public String description;
    public String url;
    public boolean editable;

    public String expenses;
    public  String currentAdd;
    Days(){
        description=url=NULL;
        expenses=NULL;
        currentAdd=NULL;
        editable=true;
    }
    Days(String desc,String imageurl,String currentAdd,String expenses) {
        this.description = desc;
        this.currentAdd = currentAdd;
        this.url = imageurl;
        this.expenses = expenses;
        editable=true;
//        startDate = new Date();
    }
}
