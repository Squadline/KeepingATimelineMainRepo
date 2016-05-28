package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    private String uidTimeline;                 //UID of timeline

    private String currentFirst;                // First name of the current user
    private String currentLast;                 // Last name of the current user
    private String currentEmail;                // Email of the current user

    // String constants for Firebase children
    private final String USERS_STR = "Users";
    private final String NAME_STR = "FirstName";
    private final String LAST_STR = "LastName";
    private final String EMAIL_STR = "EmailAddress";
    private final String DB_STR = "https://fiery-fire-8218.firebaseio.com/";

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

        // get active user id
        Firebase ref = new Firebase(DB_STR);
        holder = ref.getAuth().getUid();

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleBar = (TextView) findViewById(R.id.toolbar_title);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), getString(R.string.squadLineFont));
        titleBar.setTypeface(myCustomFont);

        // Opens sidebar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // sets name and email of current user in sidebar
        View navHeader = navigationView.getHeaderView(0);
        final TextView userName = (TextView) navHeader.findViewById(R.id.user_name);
        final TextView userEmail = (TextView) navHeader.findViewById(R.id.user_email);

        //.com/Users/uid
        //.com/Users/

        Firebase current = ref.child(USERS_STR).child(holder);

        current.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the name of  the current user
                currentFirst = dataSnapshot.child(NAME_STR).getValue().toString();
                currentLast = dataSnapshot.child(LAST_STR).getValue().toString();

                // Prevents null object reference
                currentFirst = currentFirst + "";
                currentLast = currentLast + "";

                userName.setText(currentFirst + " " + currentLast);

                // Get the email of the current user
                currentEmail = dataSnapshot.child(EMAIL_STR).getValue().toString();

                userEmail.setText(currentEmail);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Error loading data.",
                        Toast.LENGTH_SHORT).show();
            }
        });


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
                    tlFriends.add("" + tlSnapshot.getValue()); //actual title
                    tlTitles.add("" + tlSnapshot.getKey());
                    //System.out.println(tlSnapshot.getValue());
                    uidTimeline = tlSnapshot.getKey();


                    /*Firebase database2 = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines/" + uidTimeline + "/Users");
                    database2.addValueEventListener(new ValueEventListener() {
                        @Override
                        tlTitles.clear();
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userShot: dataSnapshot.getChildren())
                            {
                                tlTitles.add((String)userShot.getValue());
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });*/
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
                String timelineName = tlFriends.get(position);

                Intent viewTimelineActivity = new Intent("com.keepingatimeline.kat.ViewTimeline");
                viewTimelineActivity.putExtra("Timeline Name", timelineName);
                viewTimelineActivity.putExtra("Timeline ID", tlTitles.get(position));
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

                LayoutInflater addInflater = this.getLayoutInflater();
                View view = addInflater.inflate(R.layout.dialog_add_timeline, null);

                final EditText nameTLInput = (EditText) view.findViewById(R.id.addTimelineInput);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New Timeline");
                builder.setMessage("Enter the name for your new timeline.");
                builder.setView(view);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com/");
                        holder = ref.getAuth().getUid().toString();
                        ref = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder + "/EmailAddress");

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                emailAdd = dataSnapshot.getValue().toString();
                                newName = nameTLInput.getText().toString();
                                database = Vars.getFirebase().child("Timelines");
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
                                post1.put(Vars.getUID(), emailAdd);
                                user.setValue(post1);

                                database = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder + "/Timelines");
                                event1.put(tKey, newName);
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

        if (id == R.id.navChangeName) {

        } else if (id == R.id.navChangePassword) {
            showChangePassword(); // shows change password dialog - by me!!!
        } else if (id == R.id.navChangePhoto) {

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
            this.finish();


        }

        //closes drawer after button has been selected
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePassword(){

        final String emailStr = Vars.getFirebase().getAuth().getProviderData().get("email").toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_change_password, null);
        builder.setTitle("Change Password");
        builder.setMessage("Enter your current password, then enter a new password and confirm it.");

        builder.setView(v);

        final EditText curPass = (EditText) v.findViewById(R.id.currentPasswordInput);
        final EditText newPass = (EditText) v.findViewById(R.id.newPasswordInput);
        final EditText conPass = (EditText) v.findViewById(R.id.confirmPasswordInput);


        builder.setNegativeButton("Cancel", null);


        builder.setPositiveButton("Save", null);

        //change password dialog
        final AlertDialog changePDialog = builder.create();
        changePDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button confirm = changePDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirm.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // FORM CHECKING

                        // check if anything is empty
                        if (newPass.getText().toString().length() == 0 ||
                                curPass .getText().toString().length() == 0 ||
                                conPass.getText().toString().length() == 0) {

                        }
                        // check if new pass == confirm pass
                        if( !newPass.getText().toString().equals(conPass.getText().toString())) {

                            Toast.makeText(getApplicationContext(), "New passwords do not match! :0",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        // check check if currpass == new pass, aka no point

                        if( newPass.getText().toString().equals(curPass.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Current password same as new password!"
                                    + "Please change the password to something different!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        // SERVER CHECKING
                        Vars.getFirebase().changePassword(emailStr, curPass.getText().toString(),
                                newPass.getText().toString(), new Firebase.ResultHandler() {
                                    @Override
                                    public void onError(FirebaseError err)
                                    {
                                        if( err.getCode() == FirebaseError.INVALID_PASSWORD)
                                        {
                                            Toast.makeText(getApplicationContext(),
                                                    "Error: Current password is incorrect",
                                                    Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Error: " + err.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onSuccess()
                                    {
                                        Toast.makeText(getApplicationContext(),  "Password successfully changed",
                                                Toast.LENGTH_LONG).show();
                                        changePDialog.cancel();
                                        return;
                                    }

                                });

                /*firebaseRef.changePassword( oldPassword, nP, nPConfirm );
                {
                    if ok, close dialog and toast success
                    if bad, stay in dialog and toast oldP incorrecto o whatever
                }
                */

                        // dialog.dismiss();
                    }
                });
                Button cancel = changePDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //close this dialog
                        changePDialog.cancel();
                        return;
                    }
                });
            }



        });
        changePDialog.show();

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
}
