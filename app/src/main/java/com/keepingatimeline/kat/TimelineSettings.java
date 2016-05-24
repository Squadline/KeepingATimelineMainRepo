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
 *  Remove People     Toast, Dialog
 *  Leave Squad       Toast, Dialog
 *  Notifications
 *  Rename Timeline   Toast, Dialog
 */
public class TimelineSettings extends AppCompatActivity {

    private String currentTimelineID;           // ID of the current timeline
    private String currentName;                 // Name of the current user
    private TextView squadTitle;                // Name of timeline
    private TextView addFriend;                 // Add Friend button
    private ArrayAdapter<String> adapter;       // Adapter for list of users
    private ArrayList<String> users;            // List of user names

    private Firebase db;                        // Database object

    // String constants for Firebase children
    private final String TITLE_STR = "Title";
    private final String USERS_STR = "Users";
    private final String NAME_STR = "EmailAddress";
    private final String ID_STR = "Timeline ID";

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
                // WARNING: If case is reached, app will crash when loading data
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

        /*
         * Once getUserEmail is correctly implemented in Vars, we can remove
         * this outer listener assignment.
         */

        // Get the Firebase object of the current user
        Firebase currentUser = Vars.getUser();

        // Retrieve current user name ONCE from Firebase
        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the name of the current user
                currentName = dataSnapshot.child(NAME_STR).getValue().toString();

                // Instantiate db to current timeline once we've gotten
                // the current user's name
                db = Vars.getTimeline(currentTimelineID);

                // Add event listener to get settings updates
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get value of the title child of timeline and set title
                        squadTitle.setText(dataSnapshot.child(TITLE_STR).getValue().toString());

                        // reset the list of users and add current user to top
                        users.clear();
                        users.add(currentName);

                        // iterate through the users in the database (alphabetically)
                        // and add their names to the list of timeline users
                        // excluding the current user, whose name was added at the top
                        for (DataSnapshot member : dataSnapshot.child(USERS_STR).getChildren()) {
                            String memberName = member.getValue().toString();
                            if (!memberName.equals(currentName)) {
                                users.add(memberName);
                            }
                        }
                        // notify adapter of update and reset view
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Error loading data.",
                        Toast.LENGTH_SHORT).show();
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