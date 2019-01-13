package android.com.avishkar;

public class UserLogin {
    public String email;
    public String mob;
    public int present;
    public int past;
    public int future;
    public String name;
    public String uri;
    UserLogin()
    {

    }
    UserLogin(String email,String mob,int present,int past,int future)
    {
        this.future=future;
        this.past=past;
        this.present=present;
        this.email=email;
        this.mob=mob;
        this.name="Avatar";
        this.uri="";
    }
}
