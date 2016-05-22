package com.keepingatimeline.kat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
 */
public class TimelineSettings extends AppCompatActivity {

    private String current;                     // ID of the current timeline
    private TextView squadTitle;                // Name of timeline
    private ArrayAdapter<String> adapter;       // Adapter for list of users
    private ArrayList<String> users;            // List of user names

    private Firebase db;                        // Database object

    // String constants for Firebase children
    private final String TITLE_STR = "Title";
    private final String USERS_STR = "Users";
    private final String CURR_STR = "Current";
    private final String TIMELINE_STR = "Timelines";
    private final String DB_STR = "https://fiery-fire-8218.firebaseio.com/";

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
        NonScrollListView user_list = (NonScrollListView) findViewById(R.id.user_list);

        // Instantiate list of users and the adapter to the ListView
        users = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.settings_user_list, users);

        // Set the ListView's adapter
        user_list.setAdapter(adapter);

        // Instantiate Firebase object to main database
        db = new Firebase(DB_STR);

        // Get the UID of the currently authenticated user
        // Get the Firebase object of the timeline that the user is viewing
        String currentUser = db.getAuth().getUid();
        Firebase currentLine = db.child(USERS_STR).child(currentUser).child(CURR_STR);

        // Retrieve timeline title ONCE from Firebase
        currentLine.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the ID of the current timeline
                current = dataSnapshot.getValue().toString();

                // Move db down to current timeline once we've gotten
                // the ID of the current timeline
                db = db.child(TIMELINE_STR).child(current);

                // Add event listener to get settings updates
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get value of the title child of timeline and set title
                        squadTitle.setText(dataSnapshot.child(TITLE_STR).getValue().toString());

                        // reset the list of users and add current user to top
                        users.clear();
                        users.add("Eunji");

                        // iterate through the users in the database (alphabetically)
                        // and add their names to the list of timeline users
                        for (DataSnapshot member : dataSnapshot.child(USERS_STR).getChildren()) {
                            users.add(member.getValue().toString());
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
