package com.keepingatimeline.kat;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class AddEvent extends AppCompatActivity {

    private String timelineID;
    private String timelineName;
    private ViewPager viewPager;
    private EventPagerAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.keepingatimeline.kat.R.layout.activity_add_event);
        Firebase.setAndroidContext(this);

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView nextText = (TextView) findViewById(R.id.nextText);
        TextView cancelText = (TextView) findViewById(R.id.cancelText);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                timelineID = null;
                timelineName = "";
            } else {
                timelineID = extras.getString("Timeline ID");
                timelineName = extras.getString("Timeline Name");
            }
        } else {
            timelineID = (String) savedInstanceState.getSerializable("Timeline ID");
            timelineName = (String) savedInstanceState.getSerializable("Timeline Name");
        }

        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = Vars.getTimeline(timelineID);
                ref = ref.child("Event");
                ref = ref.push();
                Event event = new Event();
                switch(viewPager.getCurrentItem()) {
                    case 0:
                        event.setType("photo");
                        break;
                    case 1:
                        event.setType("quote");
                        break;
                    case 2:
                        event.setType("text");
                        break;
                    default:
                        event.setType("null");
                }
                ref.setValue(event);
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.eventTabs);

        viewPager = (ViewPager) findViewById(R.id.eventPager);
        eventAdapter = new EventPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(eventAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.grey500), Color.WHITE);

        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventPagerAdapter fragViewer = (EventPagerAdapter) viewPager.getAdapter();
                int position = viewPager.getCurrentItem();
                String[] data = fragViewer.getData(position);

                switch(position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                EventPagerAdapter access = (EventPagerAdapter) viewPager.getAdapter();
                access.emptyTexts(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
