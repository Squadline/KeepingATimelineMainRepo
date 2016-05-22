package com.keepingatimeline.kat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Trevor on 5/19/2016.
 */
public class EventPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public EventPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AddPhotoFragment tab1 = new AddPhotoFragment();
                return tab1;
            case 1:
                AddQuoteFragment tab2 = new AddQuoteFragment();
                return tab2;
            case 2:
                AddTextFragment tab3 = new AddTextFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
