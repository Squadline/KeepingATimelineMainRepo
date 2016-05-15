package com.keepingatimeline.kat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Initial version written by: Darren
 */
public class TimelineSettings extends AppCompatActivity {

    private TextView lineTitle;
    private ListView manageUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_settings);

        // Title of the timeline
        lineTitle =  (TextView) findViewById(R.id.title);

        // We'll get the name of the timeline in a different manner later
        lineTitle.setText("Eunji");

        // User list for timeline settings
        // This will probably be removed later when we get a
        // list of users from a different source
        ArrayList<String> users = new ArrayList<String>();
        users.add("Eunji");

        // Array of users to insert into ListView
        String[] userList = new String[users.size()];
        users.toArray(userList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, userList);

        // Get the ListView and set its contents
        manageUsers = (ListView) findViewById(R.id.user_list);
        manageUsers.setAdapter(adapter);
    }
}
