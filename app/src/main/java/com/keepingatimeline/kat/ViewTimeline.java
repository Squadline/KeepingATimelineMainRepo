package com.keepingatimeline.kat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

public class ViewTimeline extends AppCompatActivity {

    ImageButton addEvent;
    RecyclerView rv;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;

    private Firebase ref;
    private Firebase tLine;
    private Firebase tLineTitle;
    private String auth;

    private String textTBar;
    private TextView toolTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_timeline);
        Firebase.setAndroidContext(this);

        //sets title of the actionbar to the title of the timeline clicked
        ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
        auth = ref.getAuth().getUid();
        ref = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + auth);
        ref = ref.child("Current");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                auth = (String) dataSnapshot.getValue();
                tLine = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines/" + auth);
                tLineTitle = tLine.child("Title");
                tLineTitle.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        textTBar = (String) dataSnapshot.getValue();
                        TextView toolTitle = (TextView) findViewById(R.id.toolbar_title);
                        toolTitle.setText(textTBar);
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



        addEvent = (ImageButton) findViewById(R.id.addEventFAB);

        Drawable drawableFAB = ContextCompat.getDrawable(this, R.drawable.ic_add);
        drawableFAB = DrawableCompat.wrap(drawableFAB);
        DrawableCompat.setTint(drawableFAB, ContextCompat.getColor(this, R.color.white));
        addEvent.setImageDrawable(drawableFAB);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventActivity = new Intent("com.keepingatimeline.kat.AddEvent");
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

        Drawable drawableSettings = menu.findItem(R.id.editTimelineBtn).getIcon();
        drawableSettings = DrawableCompat.wrap(drawableSettings);
        DrawableCompat.setTint(drawableSettings, ContextCompat.getColor(this, R.color.white));
        menu.findItem(R.id.editTimelineBtn).setIcon(drawableSettings);

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
                startActivity(timelineSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
