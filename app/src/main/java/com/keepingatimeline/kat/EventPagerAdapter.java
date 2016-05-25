package com.keepingatimeline.kat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Trevor on 5/19/2016.
 */
public class EventPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs = 3;
    AddPhotoFragment tab1;
    AddQuoteFragment tab2;
    AddTextFragment tab3;
    private String tabTitles[] = new String[]{"Photo", "Quote", "Text"};

    public EventPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        tab1 = new AddPhotoFragment();
        tab2 = new AddQuoteFragment();
        tab3 = new AddTextFragment();
        //this.numOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    //Change Fields to Blank EditTexts
    public void emptyTexts(int position) {
        switch (position) {
            case 0:
                tab1.emptyTexts();
                break;
            case 1:
                tab2.emptyTexts();
                break;
            case 2:
                tab3.emptyTexts();
                break;
            default:
                break;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Generate title based on item position
        return tabTitles[position];
    }

    public String[] getData(int position) {
        String[] dataArray = new String[4];
        switch (position) {
            case 0:
                //Get Photo Fragment Data Fields
                dataArray[0] = tab1.getTitle();
                dataArray[1] = tab1.getDate();
                dataArray[2] = tab1.getDescription();
                dataArray[3] = tab1.getPhoto();
                break;
            case 1:
                dataArray[0] = tab2.getTitle();
                dataArray[1] = tab2.getDate();
                dataArray[2] = tab2.getQuote();
                dataArray[3] = tab2.getSource();
                break;
            case 2:
                dataArray[0] = tab3.getTitle();
                dataArray[1] = tab3.getDate();
                dataArray[2] = tab3.getText();
                break;
            default:
                break;
        }

        return dataArray;
    }
}

