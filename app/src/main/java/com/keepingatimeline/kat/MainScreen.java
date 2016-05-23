package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Firebase database;
    private ArrayList<String> tlTitles = new ArrayList<>();
    private ArrayList<String> tlFriends = new ArrayList<>();
    private TimelineAdapter inflateTimeline;
    private ListView timelineList;
    private String holder;
    private AlertDialog.Builder dialogBuilder;
    private String newName;
    private TextView titleBar;

    private String emailAdd;

    private String temp;
    private Firebase current;

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

        titleBar = (TextView) findViewById(R.id.toolbar_title);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.ttf");
        titleBar.setTypeface(myCustomFont);

        Firebase.setAndroidContext(this);

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Opens sidebar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);

        // get active user id
        Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
        holder = ref.getAuth().getUid();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

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

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //creates a pointer to from the user table to the timeline table for future use
        timelineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String timelineName = tlTitles.get(position);

                Intent viewTimelineActivity = new Intent("com.keepingatimeline.kat.ViewTimeline");
                viewTimelineActivity.putExtra("Timeline ID", tlFriends.get(position));
                startActivity(viewTimelineActivity);
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
            case android.R.id.home:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.add_timeline:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Name your new Timeline");

                // Set up the input
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
                        holder = ref.getAuth().getUid().toString();
                        ref = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder + "/EmailAddress");

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                emailAdd = dataSnapshot.getValue().toString();
                                newName = input.getText().toString();
                                database = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines");
                                database = database.push();
                                String tKey = database.getKey();
                                Map<String, String> post = new HashMap<String, String>();
                                Map<String, String> post1 = new HashMap<String, String>();
                                Map<String, String> event = new HashMap<String, String>();
                                Map<String, Object> event1 = new HashMap<String, Object>();
                                post.put("Admin", emailAdd);
                                post.put("Title", newName);
                                database.setValue(post);

                                Firebase user =  database.child("Users");
                                post1.put("Email" , emailAdd);
                                user.setValue(post1);

                                Firebase events = database.child("Events");
                                event.put("NameE", "Timeline: " + newName + " was created");
                                events.setValue(event);

                                database = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder + "/Timelines");
                                event1.put(newName, tKey);
                                database.updateChildren(event1);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                //Intent addTimelineActivity = new Intent("com.keepingatimeline.kat.Timelineshower");
                //startActivity(addTimelineActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
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
            getMainScreenHelp(); // sets up the help dialogue --Dana
        } else if (id == R.id.navLogOut) {

            Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
            CharSequence logoutToast = ref.getAuth().getProviderData().get("email") + " has logged out " + ref.getAuth().getUid();

            Toast logout = Toast.makeText(getApplicationContext(), logoutToast, Toast.LENGTH_SHORT);
            logout.show();
            ref.unauth();

            Intent loginActivity = new Intent("com.keepingatimeline.LoginActivity");
            startActivity(loginActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // displays a help dialogue --Dana
    private void getMainScreenHelp(){
        AlertDialog.Builder helpDialogBuilder = new AlertDialog.Builder(this);

        helpDialogBuilder.setTitle("Help");
        helpDialogBuilder.setMessage("Select a timeline to view it or press the '+' button to create a new timeline.\n\nWas this helpful?");
        helpDialogBuilder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
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
        helpDialogBuilder.setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialogHelp = helpDialogBuilder.create();
        dialogHelp.show();
    }

    private void addTimeLineDialog() {
        final EditText Title = new EditText(this);
        dialogBuilder = new AlertDialog.Builder(this);
        Firebase.setAndroidContext(this);

        dialogBuilder.setTitle("Add A Timeline: ");
        dialogBuilder.setMessage("Alright! What name do you want your new Timeline to be!?");
        dialogBuilder.setView(Title);
        dialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialogForgotPassword = dialogBuilder.create();
        dialogForgotPassword.show();
    }

}
