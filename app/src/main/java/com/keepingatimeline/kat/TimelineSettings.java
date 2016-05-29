package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Initial version written by: Darren
 *
 * Class: TimelineSettings
 * Purpose: Display the settings of the timeline and list of users
 *
 * TODO:
 *  Add People        Toast, Dialog             DONE
 *  Leave Squad       Dialog                    DONE
 *  Display Picture
 *  Change Picture    Toast, Dialog
 *  Rename Timeline   Dialog
 *  Notifications
 *
 *  Add null checks to prevent crashing         DONE
 *  Change member display to names
 *
 *  Toast messages or Notifications to show member changes?
 */
public class TimelineSettings extends AppCompatActivity
        implements AddFriendFragment.AddFriendListener {

    private String currentTimelineID;           // ID of the current timeline
    private String currentTimelineName;         // Name of the current timeline
    private TextView squadTitle;                // Name of timeline
    private TextView addFriend;                 // Add Friend button
    private TextView leaveSquad;                // Leave Squad button
    private ArrayAdapter<String> adapter;       // Adapter for list of users
    private ArrayList<String> users;            // List of user names

    private Firebase db;                        // Database object

    // String constants for Firebase children
    private final String TITLE_STR = "Title";
    private final String DB_USERS = "Users";                // Users table in main DB
    private final String USERS_STR = "Users";               // Users table in Timelines
    private final String TIMELINE_STR = "Timelines";
    private final String EMAIL_STR = "EmailAddress";
    private final String TIME_ID_STR = "Timeline ID";
    private final String TIME_NAME_STR = "Timeline Name";
    private final String MAIN_SCREEN = "com.keepingatimeline.kat.MainScreen";

    // Error Messages
    private final String DATA_ERR = "Error loading data.";
    private final String EMAIL_ERR = "Please enter a valid email address.";
    private final String ADD_NOT_FOUND = "Error: Email address not found.";
    private final String ADD_ALREADY_EXISTS = " is already in this Squad.";
    private final String ADD_MSG = " has been successfully added.";

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
        addFriend = (TextView) findViewById(R.id.addPeople);
        leaveSquad = (TextView) findViewById(R.id.leaveSquad);
        NonScrollListView user_list = (NonScrollListView) findViewById(R.id.user_list);

        // Set font for the title
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), getString(R.string.primaryFont));
        squadTitle.setTypeface(myCustomFont);

        // Instantiate list of users and the adapter to the ListView
        users = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.settings_user_list, users);

        // Set the ListView's adapter
        user_list.setAdapter(adapter);

        // Get the passed in extras
        // If we have no saved instance state, look at the bundle
        if (savedInstanceState == null) {
            // Get the extras bundle
            Bundle extras = getIntent().getExtras();

            // If bundle is null, then set current line as empty string
            if(extras == null) {
                // If this case is reached, nothing will be loaded in the settings
                // To fix, go back to ViewTimeline and reenter settings
                currentTimelineID = "";
                currentTimelineName = "";
            }
            else {
                // otherwise, get timeline ID and name from extras
                currentTimelineID = extras.getString(TIME_ID_STR);
                currentTimelineName = extras.getString(TIME_NAME_STR);
            }
        }
        else {
            // If we have a saved instance state, then get the ID and name from there
            currentTimelineID = (String) savedInstanceState.getSerializable(TIME_ID_STR);
            currentTimelineName = (String) savedInstanceState.getSerializable(TIME_NAME_STR);
        }

        // If the passed in timeline ID we get is null
        // (i.e. if app somehow starts on timeline settings page)
        // then notify user that data cannot be loaded
        if (currentTimelineID == null) {
            // Print out data loading error message on Toast
            showDataErrorMsg();

            // Stop creating the activity (user will exit anyway)
            return;
        }

        // Instantiate db to current timeline
        db = Vars.getTimeline(currentTimelineID);

        // Add event listener to get settings updates
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // If we get an invalid timeline ID
                // attempting to get the title will return a null value
                // Print out data loading error message on Toast and stop data retrieval
                if (dataSnapshot.child(TITLE_STR).getValue() == null ) {
                    // Print out Toast error
                    showDataErrorMsg();

                    // Perform no further actions with data
                    return;
                }

                // Get value of the title child of timeline and update title
                squadTitle.setText(dataSnapshot.child(TITLE_STR).getValue().toString());

                // Reset the list of users
                users.clear();

                // Iterate through the users in the table
                // and add their emails to the list of timeline users
                for (DataSnapshot member : dataSnapshot.child(USERS_STR).getChildren()) {
                    users.add(member.getValue().toString());
                }

                // Notify adapter of data update and reset view
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // If the Add People button is clicked, create a dialog
        addFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create the Add Friend dialog and show it
                DialogFragment dialog = new AddFriendFragment();
                dialog.show(getSupportFragmentManager(), "AddFriendFragment");
            }
        });

        // If the Leave Squad button is clicked, ask user for confirmation
        leaveSquad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                showLeaveSquadConfirm();
            }
        });
    }

    // Listener for Add Friend dialog
    // Check for invalid email format
    // nonexistent email
    // already in timeline
    // if not, add and display success

    // Positive Button Listener interface method
    @Override
    public void onDialogPositiveClick(final AddFriendFragment dialog) {
        // Get the email entered in the dialog
        final String email = dialog.getEmail();

        // Verify that it is actually an email address format
        // Evaluates to true if there is no @, or if there is no period after the @
        if (email.indexOf('@') < 0 || email.lastIndexOf('.') < email.indexOf('@')) {
            // Print out the Toast email error message
            showEmailErrorMsg();

            // No further actions will be taken
            return;
        }

        System.err.println("Email: " + email);

        // Verify that entered user is not already in the timeline
        // If the user is, print out already exists error message
        if (users.contains(email)) {
            // Print out Toast error message
            showAddAlreadyExistsMsg(email);

            // No further actions will be taken
            return;
        }

        // Get the table of users to search for the entered email
        // We only need to get this table once, so add a single value event listener
        Vars.getFirebase().child(DB_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // We are using linear search because users are not sorted in the table by email
                // Iterate through all users
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    // If the emails match (emails are case insensitive)
                    if (user.child(EMAIL_STR).getValue().toString().equalsIgnoreCase(email)) {
                        // Get the stored email of the user to retain consistency in style
                        // Entered email may have style inconsistencies with stored email
                        // that may cause problems later on
                        String userEmail = user.child(EMAIL_STR).getValue().toString();
                        // Get the user ID
                        String userID = user.getKey();

                        // New HashMap containing user information to store in the Timeline
                        HashMap<String, Object> added = new HashMap<String, Object>();

                        // Add the user's ID and email and put it in the Timeline's Users table
                        added.put(userID, userEmail);
                        Vars.getTimeline(currentTimelineID).child(USERS_STR).updateChildren(added);


                        // New HashMap containing timeline information to store in the User
                        HashMap<String, Object> newTime = new HashMap<String, Object>();

                        // Add the Timeline's ID and name and put it in the User's Timeline table
                        newTime.put(currentTimelineID, currentTimelineName);
                        Vars.getFirebase().child(DB_USERS).child(userID).child(TIMELINE_STR).updateChildren(newTime);

                        // Show Toast message for successful add and dismiss the dialog
                        showSuccessfulAddMsg(email);
                        dialog.getDialog().dismiss();

                        // Exit from method
                        return;
                    }
                }
                // If email does not correspond to an existing user, show error
                // Do not dismiss the dialog
                showAddErrorMsg();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Helper methods to create and show Toasts with Error Messages
    private void showDataErrorMsg() {
        Toast toast = Toast.makeText(getApplicationContext(), DATA_ERR, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showAddErrorMsg() {
        Toast toast = Toast.makeText(getApplicationContext(), ADD_NOT_FOUND, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showEmailErrorMsg() {
        Toast toast = Toast.makeText(getApplicationContext(), EMAIL_ERR, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showAddAlreadyExistsMsg(String user) {
        Toast toast = Toast.makeText(getApplicationContext(), user + ADD_ALREADY_EXISTS, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showSuccessfulAddMsg(String user) {
        Toast toast = Toast.makeText(getApplicationContext(), user + ADD_MSG, Toast.LENGTH_LONG);
        toast.show();
    }

    // Helper method to display Leave Squad confirmation
    private void showLeaveSquadConfirm() {
        // Create the dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title, message, and buttons
        builder.setTitle("Leave Squad")
                .setMessage("Do you really want to leave this Squad?")
                // If user does want to leave
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Create a map to remove user from Timeline's Users table
                        HashMap<String, Object> removedUser = new HashMap<String, Object>();

                        // Put a null object and update Timeline's Users table
                        // effectively removing the user from the table
                        removedUser.put(Vars.getUID(), null);
                        Vars.getTimeline(currentTimelineID).child(USERS_STR).updateChildren(removedUser);

                        // Create a map to remove timeline from User's Timelines table
                        HashMap<String, Object> removedSquad = new HashMap<String, Object>();

                        // Put a null object and update User's Timelines table
                        // effectively removing the timeline from the table
                        removedSquad.put(currentTimelineID, null);
                        Vars.getFirebase().child(DB_USERS).child(Vars.getUID()).child(TIMELINE_STR).updateChildren(removedSquad);

                        // Dismiss the dialog
                        dialog.dismiss();

                        // Create an intent to return to the Main Screen
                        // Set the flag to FLAG_ACTIVITY_CLEAR_TOP
                        // which stops every activity above it in the stack and brings it to the top
                        Intent goBackToMain = new Intent(MAIN_SCREEN);
                        goBackToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Return to the Main Screen
                        startActivity(goBackToMain);
                    }
                })
                // If user clicks cancel
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the dialog
                        dialog.cancel();
                    }
                });

        // Create and show the dialog
        builder.create().show();
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