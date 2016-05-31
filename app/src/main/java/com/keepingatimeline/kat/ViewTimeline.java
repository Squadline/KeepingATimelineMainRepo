package com.keepingatimeline.kat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
    private TextView squadTitle;

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
    private LinkedHashMap<String, List<String>> expandableListData;
    //private List<String> dates;


    // Timeline Settings request code - Darren
    private final int T_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_timeline);

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adds back button to toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white);

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

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        squadTitle = (TextView) findViewById(R.id.timeline_title);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), getString(R.string.RobotoMedium));
        squadTitle.setTypeface(myCustomFont);
        squadTitle.setText(timelineName);


        addEvent = (ImageButton) findViewById(R.id.addEventFAB);

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
        final ArrayList<Event> eventList = new ArrayList<Event>();
        rvAdapter = new EventAdapter(this, eventList, timelineID);
        rv.setAdapter(rvAdapter);

        firebaseRef = Vars.getTimeline(timelineID).child("Events");

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()){
                    Event event = eventSnapshot.getValue(Event.class);
                    Log.d("Event Key", event.getKey());
                    if(event.getType().equals("photo")) {
                        Bitmap temp = PictureCompactor.StringB64ToBitmap(event.getString2());
                        BitmapCache.addBitmapToMemoryCache(event.getKey(), temp);
                    }
                    eventList.add(event);
                }
                //this is where eventList needs to undergo sorting
                mergeSort(eventList);
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //find the specified drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mActivityTitle = getTitle().toString();

        //find the specified right drawer
        mExpandableListView = (ExpandableListView) findViewById(R.id.right_drawer);
        //mSelectedItemView = (TextView) findViewById(R.id.selected_item);

        Firebase fb = Vars.getTimeline(timelineID);
        //dates = new ArrayList<>();

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                for (DataSnapshot date: dataSnapshot.child("Events").getChildren())
                {
                    String curr_date = date.child("date").getValue().toString();
                    dates.add(curr_date);
                }
                */
                //update method
                mExpandableListData = getData(eventList);
                mExpandableListTitle = new ArrayList(mExpandableListData.keySet());
                addDrawerItems(eventList);
                ((CustomExpandableListAdapter)mExpandableListView.getExpandableListAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
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
                // Changed this in order to get changed name back from settings - Darren
                startActivityForResult(timelineSettingsActivity, T_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Start of Darren's modifications

    /*
    * This is only needed because the timeline name for this activity is
    * passed in from the Main Screen, so it never contacts the server for the name.
    * When the name is changed in the Timeline Settings, we will need to
    * update the displayed name here as well.
    * Thus, we will need to listen for the result of the Timeline Settings activity
    * If it returns the signal result, then it signifies that we have to update the name.
    */

    // Overridden method for activity result listener
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Call super method first and foremost
        super.onActivityResult(requestCode, resultCode, data);

        // If the finished activity was Timeline Settings
        if (requestCode == T_SETTINGS) {
            // And it returned the signal result code
            if (resultCode == RESULT_OK) {
                // Change the locally stored timeline name
                timelineName = data.getStringExtra("Timeline Name");
            }
        }
    }

    // Overridden resume method
    @Override
    public void onResume() {
        // Call the super method
        super.onResume();
        // Update the displayed timeline name
        squadTitle.setText(timelineName);
    }

    // End of Darren's modifications


    //add the items inside the drawer
    private void addDrawerItems(final ArrayList<Event> whole) {

        //takes in the object itself, a lit of titles, and all the date related to the title
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);

        //sets data behind this customizable drawer view
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                int index = 0;
                int index_2 = 0;
                String first = "";
                String second = "";

                for(String key : expandableListData.keySet())
                {
                    if(index == groupPosition)
                    {
                        first = key;
                        for(String str : expandableListData.get(first))
                        {
                            if(index_2 == childPosition)
                            {
                                second = str;
                                break;
                            }
                            index_2++;
                        }
                        break;
                    }
                    index++;
                }

                String month = "";
                int year = 0;
                int num_month = 0;

                String[] part = first.split("(?=\\d)(?<!\\d)");

                month = part[0];
                month = month.replaceAll("\\s+","");
                year = Integer.parseInt(part[1]);

                String[] Months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                for(int i = 0; i < Months.length; i++)
                {
                    if(month.equals(Months[i]))
                    {
                        num_month = i;
                    }
                }

                String result = Integer.toString(num_month)+ "/" + second + "/" + Integer.toString(year) ;

                System.out.println(result);

                for( int i = 0; i < whole.size(); i++ )
                {
                    //System.out.println(whole.get(i).getDate());
                    if(whole.get(i).getDate().equals(result))
                    {
                        rv.smoothScrollToPosition(i);
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public Map<String, List<String>> getData(ArrayList<Event> whole) {

        //sorts the dates
        expandableListData = new LinkedHashMap<>();

        String[] Months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        int month_data = 0;
        int day_data = 0;
        int year_data = 0;
        List<String> months_date = new ArrayList<String>();
        String curr_m_y = "";
        int temp_day = 0;

        //Goes through all the given dates
        for(int i = 0; i < whole.size(); i++)
        {
            //Separates given string into pieces using scanner
            Scanner scanner = new Scanner(whole.get(i).getDate()).useDelimiter("[^0-9]+");
            month_data = scanner.nextInt();
            day_data = scanner.nextInt();
            year_data = scanner.nextInt();

            //Gets the month you are in right now
            String curr_month = Months[month_data];

            //Strings together the month and the year together
            String month_year = curr_month + " " + Integer.toString(year_data);

            //System.err.println(month_year);
            if(months_date.isEmpty())
            {
                curr_m_y = month_year;
                months_date.add(Integer.toString(day_data));
                temp_day = day_data;
                if(whole.size()-1 == i)
                {
                    expandableListData.put(month_year, new ArrayList<String>(months_date));
                }
            }
            else if(month_year.equals(curr_m_y))
            {

                if( !(temp_day == day_data) )
                {
                    months_date.add(Integer.toString(day_data));
                    temp_day = day_data;
                }
                if(whole.size()-1 == i)
                {
                    expandableListData.put(month_year, new ArrayList<String>(months_date));
                }
            }
            else
            {
                expandableListData.put(curr_m_y, new ArrayList<String>(months_date));
                curr_m_y = month_year;
                months_date.clear();
                months_date.add(Integer.toString(day_data));
                temp_day = day_data;
                if(whole.size()-1 == i)
                {
                    expandableListData.put(month_year, new ArrayList<String>(months_date));
                }
            }
        }
        Iterator<Map.Entry<String, List<String>>> s = expandableListData.entrySet().iterator();

        return expandableListData;
    }



    private ArrayList<Event> mergeSort(ArrayList<Event> whole) {
        ArrayList<Event> left = new ArrayList<Event>();
        ArrayList<Event> right = new ArrayList<Event>();
        int center;

        if (whole.size() <= 1) {
            return whole;
        } else {
            center = whole.size()/2;
            // copy the left half of whole into the left.
            for (int i=0; i<center; i++) {
                left.add(whole.get(i));
            }

            //copy the right half of whole into the new arraylist.
            for (int i=center; i<whole.size(); i++) {
                right.add(whole.get(i));
            }

            // Sort the left and right halves of the arraylist.
            left  = mergeSort(left);
            right = mergeSort(right);

            // Merge the results back together.
            merge(left, right, whole);
        }
        return whole;
    }

    private void merge(ArrayList<Event> left, ArrayList<Event> right, ArrayList<Event> whole) {
        int leftIndex = 0;
        int rightIndex = 0;
        int wholeIndex = 0;

        // As long as neither the left nor the right ArrayList has
        // been used up, keep taking the smaller of left.get(leftIndex)
        // or right.get(rightIndex) and adding it at both.get(bothIndex).
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if ( !DateGen.compareDates(left.get(leftIndex).getDate(),
                    right.get(rightIndex).getDate())) {
                whole.set(wholeIndex, left.get(leftIndex));
                leftIndex++;
            } else {
                whole.set(wholeIndex, right.get(rightIndex));
                rightIndex++;
            }
            wholeIndex++;
        }

        ArrayList<Event> rest;
        int restIndex;
        if (leftIndex >= left.size()) {
            // The left ArrayList has been used up...
            rest = right;
            restIndex = rightIndex;
        } else {
            // The right ArrayList has been used up...
            rest = left;
            restIndex = leftIndex;
        }

        // Copy the rest of whichever ArrayList (left or right) was not used up.
        for (int i=restIndex; i<rest.size(); i++) {
            whole.set(wholeIndex, rest.get(i));
            wholeIndex++;
        }
    }
}
