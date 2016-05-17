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
 */
public class TimelineSettings extends AppCompatActivity {

    private TextView lineTitle;
    private ListView manageUsers;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> users;

    private Firebase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_settings);

        // Title of the timeline
        lineTitle =  (TextView) findViewById(R.id.title);
        manageUsers = (ListView) findViewById(R.id.user_list);

        users = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, users);

        // We'll get the name of the timeline in a different manner later

        // User list for timeline settings
        // This will probably be removed later when we get a
        // list of users from a different source
        db = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines/-KHqAusltT6snczElia5");

        db.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nameSnap = dataSnapshot.child("name");
                lineTitle.setText( nameSnap.getValue().toString() );

                users.clear();
                users.add("Eunji");

                for ( DataSnapshot member : dataSnapshot.child("users").getChildren() ) {
                    users.add( member.getKey() );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Get the ListView and set its contents
        manageUsers.setAdapter(adapter);
    }
}
