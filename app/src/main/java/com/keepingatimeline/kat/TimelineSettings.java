package com.keepingatimeline.kat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adds back button to toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable drawableBack = ContextCompat.getDrawable(this, R.drawable.ic_chevron_left);
        drawableBack = DrawableCompat.wrap(drawableBack);
        DrawableCompat.setTint(drawableBack, ContextCompat.getColor(this, R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(drawableBack);
/*
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
        db = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines/-KIL5xP01qXU_6aDtzly");

        db.addValueEventListener( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nameSnap = dataSnapshot.child("Title");
                lineTitle.setText( nameSnap.getValue().toString() );

                users.clear();
                users.add("Eunji");

                for ( DataSnapshot member : dataSnapshot.child("Users").getChildren() ) {
                    users.add( member.getKey() );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Get the ListView and set its contents
        manageUsers.setAdapter(adapter);*/
    }

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
