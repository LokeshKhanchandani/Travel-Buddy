package android.com.avishkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.majeur.cling.Cling;
import com.majeur.cling.ClingManager;
import com.majeur.cling.ViewTarget;

public class Dashboard extends AppCompatActivity  {
    public static String signinemail;
    private android.support.v7.widget.Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mToolBar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        signinemail = getIntent().getExtras().getString("email");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.navigation_view);
        show_tut();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,mToolBar,
                R.string.navigation_draw_open, R.string.navigation_draw_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ViewPagerFragment()).commit();
        }
    }
    public String getEmail()
    {
        return signinemail;
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    public void tripClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new ViewPagerFragment()).commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void profileClick(View view) {
        Intent intent = new Intent(getApplicationContext(),Profile.class);
        startActivity(intent);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void exploreClick(View view) {
        Toast.makeText(Dashboard.this,"Explore",Toast.LENGTH_SHORT).show();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent intent1 = new Intent(Dashboard.this,Main2Activity.class);
        startActivity(intent1);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void homeClick(View view) {
        Toast.makeText(Dashboard.this,"Home",Toast.LENGTH_SHORT).show();
        Intent homeIntent = new Intent(Dashboard.this,Home.class);
        homeIntent.putExtra("id",1);
        homeIntent.putExtra("email",signinemail);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        startActivity(homeIntent);
        finish();
    }


    public void settingsClick(View view) {
        Toast.makeText(Dashboard.this,"Settings",Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
    private void show_tut()
    {
        ClingManager clingManager = new ClingManager(this);
        clingManager.addCling(new Cling.Builder(this)
                .setTarget(new ViewTarget(this,R.id.saveCurrentTour))
                .setTitle("Add a new Trip")
                .setMessageBackground(getResources().getColor(R.color.profile))
                .setContent("Add and save your experiences here")
                .build());
        clingManager.start();
    }
}
