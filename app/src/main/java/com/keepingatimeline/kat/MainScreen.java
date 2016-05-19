package com.keepingatimeline.kat;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Firebase database;
    private ArrayList<String> tlTitles = new ArrayList<>();
    private ArrayList<String> tlFriends = new ArrayList<>();
    private TimelineAdapter inflateTimeline;
    private ListView timelineList;
    private String holder;

    /**
     * By: Dana, Byung, Jimmy, Trevor
     * Description:
     *      Contains various snippets of code that implement the following functionality:
     *          Left Scrollbar (account settings and such)
     *          Right Scrollbar (to be moved when timeline function becomes fully functional)
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Firebase.setAndroidContext(this);

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

<<<<<<< HEAD
        // get active user id
        Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
        holder = ref.getAuth().getUid();

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
=======
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
>>>>>>> 6003d3609b29264d2833a48c80e73a8b714c76ef

        //The left scroll bar containing account settings, log out and such
        String[] settings = {"Settings", "Add Event Test", "Timeline Settings Test", "Log Out"};
        ListView myList = (ListView) findViewById(R.id.left_drawer);
        myList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,settings));
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch( position ) {
                    case 0:
                        Intent newActivity = new Intent("com.keepingatimeline.kat.AccountSettings");
                        startActivity(newActivity);
                        break;

                    case 1:
                        Intent addEventActivity = new Intent("com.keepingatimeline.kat.AddEvent");
                        startActivity(addEventActivity);
                        break;
                    case 2:
                        Intent timelineSettingsActivity = new Intent("com.keepingatimeline.kat.TimelineSettings");
                        startActivity(timelineSettingsActivity);
                        break;
                    case 3:
                        Firebase dref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
                        CharSequence t = dref.getAuth().getProviderData().get("email") + " has logged out ";
                        int time = Toast.LENGTH_LONG;
                        Toast logout = Toast.makeText(getApplicationContext(), t, time);
                        logout.show();
                        dref.unauth();
                        Intent loginActivity = new Intent("com.keepingatimeline.LoginActivity");
                        startActivity(loginActivity);


                }
            }
        });
        */


        inflateTimeline = new TimelineAdapter(this, tlTitles, tlFriends);
        timelineList = (ListView) findViewById(R.id.timelineList);
        timelineList.setAdapter(inflateTimeline);

        // moved this from onStart() --Dana
        //set url reference to active user
        database = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder + "/Timelines");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DB_Load", "Asking Firebase for data.");
                tlTitles.clear();
                tlFriends.clear();
                for (DataSnapshot tlSnapshot: dataSnapshot.getChildren()){
                    tlTitles.add(tlSnapshot.getKey());
                    tlFriends.add("" + tlSnapshot.getValue());
                }
                inflateTimeline.notifyDataSetChanged();

                //testing if you can get active user
                System.out.println(database.getAuth().getUid());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        inflateTimeline.notifyDataSetChanged(); //updates adapter --Dana
    }

    /**
     * Trevor's shit: Creates Buttons on Toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_timeline:
                Intent addTimelineActivity = new Intent("com.keepingatimeline.kat.Timelineshower");
                startActivity(addTimelineActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        } else if (id == R.id.navLogOut) {

            Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
            CharSequence logoutToast = ref.getAuth().getProviderData().get("email") + " has logged out " + ref.getAuth().getUid();

            Toast logout = Toast.makeText(getApplicationContext(), logoutToast, Toast.LENGTH_SHORT);
            logout.show();
            ref.unauth();

            Intent loginActivity = new Intent("com.keepingatimeline.LoginActivity");
            startActivity(loginActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
