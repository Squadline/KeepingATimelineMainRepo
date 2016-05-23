package com.keepingatimeline.kat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ViewTimeline extends AppCompatActivity {

    ImageButton addEvent;
    RecyclerView rv;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;

    private Firebase firebaseRef;
    private Firebase tLine;
    private Firebase tLineTitle;
    private String auth;
    private String timelineID;
    private String timelineName;

    private List<Event> events;

    private String textTBar;
    private TextView toolTitle;

    //allows for drawer views to be pulled out from one or both vertical edges of the window
    private DrawerLayout mDrawerLayout;
    //private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private ExpandableListView mExpandableListView;
    //link between a set of data and the AdapterView that displays the data
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private Map<String, List<String>> mExpandableListData;
    private TextView mSelectedItemView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_timeline);

        //find the specified drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mActivityTitle = getTitle().toString();

        //find the specified right drawer
        mExpandableListView = (ExpandableListView) findViewById(R.id.right_drawer);
        //mSelectedItemView = (TextView) findViewById(R.id.selected_item);

        //fetches all the data inside the class (uses the title as data) using the current object
        mExpandableListData = getData();

        //keyset method used to get a set view of the keys contained inside the map
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        //sets up the items inside the drawer
        addDrawerItems();

        Firebase.setAndroidContext(this);

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

        //sets title of the actionbar to the title of the timeline clicked
        firebaseRef = Vars.getTimeline(timelineID);
        auth = Vars.getUID();

        toolTitle = (TextView) findViewById(R.id.timeline_title);
        toolTitle.setText(timelineName);

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adds back button to toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white);


        addEvent = (ImageButton) findViewById(R.id.addEventFAB);
        addEvent.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_white));

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventActivity = new Intent("com.keepingatimeline.kat.AddEvent");
                addEventActivity.putExtra("Timeline Name", timelineName);
                addEventActivity.putExtra("Timeline ID", timelineID);
                startActivity(addEventActivity);
            }
        });

        rv = (RecyclerView) findViewById(R.id.timeline_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // use a linear layout manager
        rvLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(rvLayoutManager);

        // specify an adapter (see also next example)
        rvAdapter = new EventAdapter(new String[0]);
        rv.setAdapter(rvAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.editTimelineBtn:
                Intent timelineSettingsActivity = new Intent("com.keepingatimeline.kat.TimelineSettings");
                timelineSettingsActivity.putExtra("Timeline Name", timelineName);
                timelineSettingsActivity.putExtra("Timeline ID", timelineID);
                startActivity(timelineSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //add the items inside the drawer
    private void addDrawerItems() {

        //takes in the object itself, a lit of titles, and all the date related to the title
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);

        //sets data behind this customizable drawer view
        mExpandableListView.setAdapter(mExpandableListAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static Map<String, List<String>> getData() {
        //Todo: Conversion from raw date date to the separate strings and such
        //Given that the data will be formatted in such a way like 5/19/2015 as a string
        //Somehow, I need to fetch all the raw date date from the database
        //Then i would need to iterate through them and separate everything.

        Map<String, List<String>> expandableListData = new TreeMap<>();

        String[] GivenDates = {"5/7/14", "5/9/14", "5/11/14", "5/15/14", "6/12/14", "6/14/14", "7/14/14", "8/15/15", "1/24/16"};
        String[] Months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        int month_data = 0;
        int date_data = 0;
        int year_data = 0;
        List<String> months_date = new ArrayList<String>();
        String checker = "";

        //Goes through all the given dates
        for(int i = 0; i < GivenDates.length; i++)
        {
            //Separates given string into pieces using scanner
            Scanner scanner = new Scanner(GivenDates[i]).useDelimiter("[^0-9]+");
            month_data = scanner.nextInt();
            date_data = scanner.nextInt();
            year_data = scanner.nextInt();

            //Gets the month you are in right now
            String curr_month = Months[month_data];

            //Strings together the month and the year together
            String month_year = curr_month + " " + Integer.toString(year_data);

            //initial case when the list is empty
            if(months_date.isEmpty())
            {
                months_date.add(Integer.toString(date_data));
                checker = month_year;
                continue;
            }

            if(checker.equals(month_year))
            {
                months_date.add(Integer.toString(date_data));
                continue;
            }

            //case where you are at the last position in the list
            if(i == GivenDates.length-1 && !checker.equals(month_year))
            {
                expandableListData.put(checker, months_date);
                months_date.clear();
                months_date.add(Integer.toString(date_data));
                expandableListData.put(month_year, months_date);
                continue;
            }

            if(!checker.equals(month_year))
            {
                expandableListData.put(checker, months_date);
                months_date.clear();
                months_date.add(Integer.toString(date_data));
                checker = month_year;
                continue;
            }
        }
        return expandableListData;
    }
}
