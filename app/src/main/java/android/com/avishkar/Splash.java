package android.com.avishkar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Splash extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {
//        ActionBar actionB


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        configSplash.setBackgroundColor(R.color.splash);
        configSplash.setAnimCircularRevealDuration(3000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.lelogo);
        configSplash.setAnimLogoSplashDuration(3000);
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce);

        configSplash.setTitleSplash("");
        configSplash.setTitleTextColor(R.color.colorAccentTrans);
        configSplash.setTitleTextSize(0f);
        configSplash.setAnimTitleDuration(0);
        configSplash.setAnimTitleTechnique(Techniques.Flash);
    }

    @Override
    public void animationsFinished() {
        startActivity(new Intent(Splash.this,Main2Activity.class));
        finish();
    }
}
