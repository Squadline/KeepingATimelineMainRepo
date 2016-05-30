package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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
 *  Auto-delete       Warning                   DONE
 *  Display Picture
 *  Change Picture    Toast, Dialog
 *  Rename Timeline   Dialog                    DONE
 *  Notifications
 *
 *  Add null checks to prevent crashing         DONE
 *  Change member display to names              DONE
 *  Add cancelled error messages                DONE
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
    private Switch notifications;               // Notifications switch
    private ArrayAdapter<String> adapter;       // Adapter for list of users
    private ArrayList<String> users;            // List of user emails (for existing user comparison)
    private ArrayList<String> userNames;        // List of user names  (for user display)
    private ArrayList<String> userIDs;          // List of user IDs    (for easy iteration when searching)

    private Firebase db;                        // Database object

    private boolean returnName;                 // Whether or not to pass back timeline name
    private boolean deleted;                    // Whether or not this timeline has been deleted

    // String constants for Firebase children
    private final String TITLE_STR = "Title";
    private final String DB_USERS = "Users";                // Users table in main DB
    private final String USERS_STR = "Users";               // Users table in Timelines
    private final String TIMELINE_STR = "Timelines";
    private final String FIRST_NAME = "FirstName";
    private final String LAST_NAME = "LastName";
    private final String EMAIL_STR = "EmailAddress";
    private final String TIME_ID_STR = "Timeline ID";
    private final String TIME_NAME_STR = "Timeline Name";
    private final String MAIN_SCREEN = "com.keepingatimeline.kat.MainScreen";

    // Error Messages
    private final String DATA_ERR = "Error loading data.";
    private final String RET_ERR = "Error retrieving table.";
    private final String EMAIL_ERR = "Please enter a valid email address.";
    private final String ADD_NOT_FOUND = "ERROR: User not found.";
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
        notifications = (Switch) findViewById(R.id.notifications);
        NonScrollListView user_list = (NonScrollListView) findViewById(R.id.user_list);

        // Set font for the title
        Typeface squadTitleFont = Typeface.createFromAsset(getAssets(), getString(R.string.RobotoMedium));
        squadTitle.setTypeface(squadTitleFont);

        // By default, there is no reason to return the name
        returnName = false;

        // By default, the timeline exists
        deleted = false;

        // Instantiate list of user emails, names, IDs, and the adapter to the ListView
        // Assign the name list to the adapter, since that's what we will display
        users = new ArrayList<String>();
        userNames = new ArrayList<String>();
        userIDs = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.settings_user_list, userNames);

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
                // If the timeline has been deleted, return
                // This prevents error messages from appearing
                // after a timeline has been automatically deleted
                if (deleted) {
                    return;
                }

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

                // Reset the list of user emails and IDs
                users.clear();
                userIDs.clear();

                // Iterate through the users in the table
                // and add their emails and IDs to the lists of timeline users
                for (DataSnapshot member : dataSnapshot.child(USERS_STR).getChildren()) {
                    users.add(member.getValue().toString());
                    userIDs.add(member.getKey());
                }

                // Update the list of user names and the display
                updateUserNames();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // If load was canceled, informed user of error
                showDataErrorMsg();
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

    // Helper method to update list of timeline users' names

    // Using this method will slow down timeline users retrieval
    // since we are relying on two listeners for the database
    // as well as downloading a snapshot of the Users table
    // This method is for displaying user names instead of emails
    private void updateUserNames() {
        // Clear the list of user names
        userNames.clear();

        // Get the users table in the database
        Firebase allUsers = Vars.getFirebase().child(DB_USERS);

        // Retrieve a snapshot of the users ONCE
        allUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Visit all users in the timeline and add their names to the list
                for (String UID : userIDs) {
                    // Get the name values of the user
                    String firstName = dataSnapshot.child(UID).child(FIRST_NAME).getValue().toString();
                    String lastName = dataSnapshot.child(UID).child(LAST_NAME).getValue().toString();

                    // Put a space in between and to the name list
                    userNames.add(firstName + " " + lastName);
                }
                // Notify the adapter of data update and refresh the view
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // If load was canceled, inform user of error
                showDataErrorMsg();
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
                // If we can't get the users table, then inform user of error
                showRetErrorMsg();
            }
        });
    }

    // Helper methods to create and show Toasts with Error Messages
    private void showDataErrorMsg() {
        Toast toast = Toast.makeText(getApplicationContext(), DATA_ERR, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showRetErrorMsg() {
        Toast toast = Toast.makeText(getApplicationContext(), RET_ERR, Toast.LENGTH_LONG);
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

        // Create a string for deletion warning
        String warning = "";

        // If current user is the last one in the Squad, inform them of auto-deletion
        if (users.size() == 1) {
            warning += "\n\nWARNING: You are the only user in this squad. If you leave, this squad"
                                + " will be permanently deleted.";
        }

        // Set the title, message, and buttons
        builder.setTitle("Leave Squad")
                .setMessage("Do you really want to leave this squad?" + warning)
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

                        // If the current user was the only member
                        // the timeline will be automatically deleted
                        if (users.size() == 1) {
                            // Set the value of the timeline in the database to null
                            // effectively deleting the timeline
                            Vars.getTimeline(currentTimelineID).setValue(null);
                            // Set the deleted flag to true to prevent
                            // Toast data error messages from appearing
                            deleted = true;
                        }

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

    // Helper method to show a Change Title dialog
    private void showRenameDialog() {
        // Create the dialog builder and layout inflater
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the dialog xml
        // Parent is null because it is a dialog layout
        View view = inflater.inflate(R.layout.dialog_rename_timeline, null);

        // Get the EditText field in the dialog and preset it to current Timeline name
        final EditText squadNameInput = (EditText) view.findViewById(R.id.squadNameInput);
        squadNameInput.setText(currentTimelineName);

        // Disable autocomplete and autocorrect for the title field
        squadNameInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        // Set the view, title, message, and buttons for the dialog
        builder.setView(view)
                .setTitle("Rename Squad")
                .setMessage("Enter a new name for the squad.")
                // If the user confirms the title change
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If the same name was entered, then take no action
                        if (squadNameInput.getText().toString().equals(currentTimelineName)) {
                            return;
                        }

                        // Set the current timeline name to the entered one
                        // Both locally and in the database
                        currentTimelineName = squadNameInput.getText().toString();
                        Vars.getTimeline(currentTimelineID).child(TITLE_STR).setValue(currentTimelineName);

                        // Get the list of the current timeline users
                        // We have to correct the timeline names on the user side as well
                        Firebase currentUsers = Vars.getTimeline(currentTimelineID).child(USERS_STR);

                        // Get a snapshot of the list of users ONCE
                        currentUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Iterate through all the users
                                for (DataSnapshot timelineUsers : dataSnapshot.getChildren()) {
                                    // Get the user's UID
                                    String userID = timelineUsers.getKey();
                                    // Change the name of the timeline inside their Timelines table
                                    Vars.getFirebase().child(DB_USERS).child(userID).child(TIMELINE_STR)
                                            .child(currentTimelineID).setValue(currentTimelineName);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                // If we can't get Timeline users table, then inform user of error
                                showRetErrorMsg();
                            }
                        });
                        // Timeline name has been changed
                        // There is a reason to return the name now
                        returnName = true;
                    }
                })
                // If the user presses cancel
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_settings_menu, menu);
        return true;
    }

    // Trevor's Stuff - Back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Rerouted to back button listener method - Darren
                onBackPressed();
                return true;
            case R.id.changeSquadName:
                // Show Change Title dialog
                showRenameDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Darren's stuff - Overridden back button listener

    @Override
    public void onBackPressed() {
        // If we want to return the name
        if (returnName) {
            // Create a new Intent and store the name as an extra
            Intent intent = new Intent();
            intent.putExtra(TIME_NAME_STR, currentTimelineName);
            // Set the signal result code to notify parent Activity to use data
            setResult(RESULT_OK, intent);
        }
        // Finish the activity
        finish();
    }
}