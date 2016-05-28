package com.keepingatimeline.kat;

import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Initial version written by: Darren
 *
 * Class: TimelineSettings
 * Purpose: Display the settings of the timeline and list of users
 *
 * TODO:
 *  Add People        Toast, Dialog
 *  Leave Squad       Toast, Dialog
 *  Notifications
 *  Rename Timeline   Toast, Dialog
 *  Change Picture    Toast, Dialog
 *  Display Picture
 *
 *  Add null checks to prevent crashing
 *  Change member display to names
 */
public class TimelineSettings extends AppCompatActivity
        implements AddFriendFragment.AddFriendListener {

    private String currentTimelineID;           // ID of the current timeline
    private TextView squadTitle;                // Name of timeline
    private TextView addFriend;                 // Add Friend button
    private ArrayAdapter<String> adapter;       // Adapter for list of users
    private ArrayList<String> users;            // List of user names

    private Firebase db;                        // Database object

    // String constants for Firebase children
    private final String TITLE_STR = "Title";
    private final String USERS_STR = "Users";
    private final String ID_STR = "Timeline ID";
    private final String ERR_MSG = "Error loading data.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call super method and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_settings);

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adds back button to toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white);

        // Get view objects of the activity
        squadTitle =  (TextView) findViewById(R.id.squad_title);
        addFriend = (TextView) findViewById(R.id.addPeople);
        NonScrollListView user_list = (NonScrollListView) findViewById(R.id.user_list);

        // Set font for the title
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), getString(R.string.primaryFont));
        squadTitle.setTypeface(myCustomFont);

        // Instantiate list of users and the adapter to the ListView
        users = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.settings_user_list, users);

        // Set the ListView's adapter
        user_list.setAdapter(adapter);

        // Get the passed in extras
        // If we have no saved instance state, look at the bundle
        if (savedInstanceState == null) {
            // Get the extras bundle
            Bundle extras = getIntent().getExtras();

            // If bundle is null, then set current line as empty string
            if(extras == null) {
                // If this case is reached, nothing will be loaded in the settings
                // To fix, go back to ViewTimeline and reenter settings
                currentTimelineID = "";
            }
            else {
                // otherwise, get timeline ID from extras
                currentTimelineID = extras.getString(ID_STR);
            }
        }
        else {
            // If we have a saved instance state, then get the ID from there
            currentTimelineID = (String) savedInstanceState.getSerializable(ID_STR);
        }

        // If the passed in timeline ID we get is null
        // (i.e. if app somehow starts on timeline settings page)
        // then notify user that data cannot be loaded
        if (currentTimelineID == null) {
            // Print out data loading error message on Toast
            showDataErrorMsg();

            // Stop creating the activity (user will exit anyway)
            return;
        }

        // Instantiate db to current timeline
        db = Vars.getTimeline(currentTimelineID);

        // Add event listener to get settings updates
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // If we get an invalid timeline ID
                // attempting to get the title will return a null value
                // Print out data loading error message on Toast and stop data retrieval
                if (dataSnapshot.child(TITLE_STR).getValue() == null ) {
                    // Print out Toast error
                    showDataErrorMsg();

                    // Perform no further actions with data
                    return;
                }

                // Get value of the title child of timeline and update title
                squadTitle.setText(dataSnapshot.child(TITLE_STR).getValue().toString());

                // Reset the list of users
                users.clear();

                // Iterate through the users in the database
                // and add their names to the list of timeline users
                for (DataSnapshot member : dataSnapshot.child(USERS_STR).getChildren()) {
                    users.add(member.getValue().toString());
                }

                // Notify adapter of data update and reset view
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AddFriendFragment();
                dialog.show(getSupportFragmentManager(), "addFriend");
            }
        });
    }

    // Listener for Add Friend dialog
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String em = ((AddFriendFragment)dialog).getEmail();
        System.err.println("Email: " + em);
        Vars.getFirebase().child("Users").child(em).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    System.err.println("Caught null");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Helper method to create and show a Toast with Data Loading Error Message
    private void showDataErrorMsg() {
        Toast toast = Toast.makeText(getApplicationContext(), ERR_MSG, Toast.LENGTH_LONG);
        toast.show();
    }


    // Trevor's Stuff - Back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}