package com.keepingatimeline.kat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    private TextView lineTitle;                 // Name of timeline
    private ListView manageUsers;               // ListView of users
    private ArrayAdapter<String> adapter;       // Adapter for list of users
    private ArrayList<String> users;            // List of user names

    private Firebase db;                        // Database object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call super method and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_settings);

        // Get view objects of the activity
        lineTitle =  (TextView) findViewById(R.id.title);
        manageUsers = (ListView) findViewById(R.id.user_list);

        // Instantiate list of users and the adapter to the ListView
        users = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, users);

        // Set the ListView's adapter
        manageUsers.setAdapter(adapter);

        // Get the timeline's database object
        db = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines/-KHqAusltT6snczElia5");

        // Add listener to get data of the timeline
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get name child of timeline and set title
                DataSnapshot nameSnap = dataSnapshot.child("name");
                lineTitle.setText(nameSnap.getValue().toString());

                // reset the list of users and add current user to top
                users.clear();
                users.add("Eunji");

                // iterate through the users in the database (alphabetically)
                // and add their names to the list of timeline users
                for (DataSnapshot member : dataSnapshot.child("users").getChildren()) {
                    users.add(member.getKey());
                }
                // notify adapter of update and reset view
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
