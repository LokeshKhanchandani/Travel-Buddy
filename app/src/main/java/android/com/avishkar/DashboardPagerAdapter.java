package android.com.avishkar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DashboardPagerAdapter extends FragmentStatePagerAdapter {
   public  int NUMBER_OF_PAGES = 2;
   public DashboardPagerAdapter(FragmentManager fm) {
       super(fm);
   }

   @Override
   public Fragment getItem(int position) {
       if(position ==0)


           return new MyTripFragment();
       else
           return new UpcommingFragment();
   }

   @Override
   public int getCount() {
       return  NUMBER_OF_PAGES ;
   }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "My Trips";
        else
            return "Upcomming Trips";
    }
}
