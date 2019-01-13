package android.com.avishkar;

import java.io.Serializable;

/**
 * Created by lokesh on 23/9/18.
 */

public class Travel implements Serializable {
    public String form_add;
    public String mname;
    public String rating;
    public String open;
    Travel()
    {

    }
    Travel(String form_add,String name,String rating,String open)
    {
        this.form_add=form_add;
        this.mname=name;
        this.open=open;
        this.rating=rating;
    }
}
