package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class ViewTimeline extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton addEvent;
    RecyclerView rv;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_arrow_left);

        addEvent = (ImageButton) findViewById(R.id.addEventFAB);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                startActivity(timelineSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navChangeEmail) {

        } else if (id == R.id.navChangePassword) {

        } else if (id == R.id.navShare) {

        } else if (id == R.id.navHelp) {
            getTimelineViewScreenHelp(); // sets up the help dialogue --Dana
        } else if (id == R.id.navLogOut) {

            Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
            CharSequence logoutToast = ref.getAuth().getProviderData().get("email") + " has logged out " + ref.getAuth().getUid();

            Toast logout = Toast.makeText(getApplicationContext(), logoutToast, Toast.LENGTH_SHORT);
            logout.show();
            ref.unauth();

            Intent loginActivity = new Intent("com.keepingatimeline.LoginActivity");
            startActivity(loginActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.timeline_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.timeline_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //shows help dialogue --Dana
    private void getTimelineViewScreenHelp() {
        AlertDialog.Builder helpDialogBuilder = new AlertDialog.Builder(this);

        helpDialogBuilder.setTitle("Help");
        helpDialogBuilder.setMessage("We're sorry, but NO ONE CAN HELP YOU NOW...\n\nWas this helpful?");
        helpDialogBuilder.setPositiveButton("Yes...?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        /* helpDialogBuilder.setNeutralButton("A Bit...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }); */
        helpDialogBuilder.setNegativeButton("Hell no!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialogHelp = helpDialogBuilder.create();
        dialogHelp.show();
    }
}
