package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Initial version written by: Darren
 *
 * Class: TimelineSettings
 * Purpose: Display the settings of the timeline and list of users
 *
 *  Add People        Toast, Dialog             DONE
 *  Leave Squad       Dialog                    DONE
 *  Auto-delete       Warning                   DONE
 *  Display Picture
 *  Change Picture    Toast, Dialog
 *  Rename Timeline   Dialog                    DONE
 *  Notifications                               KINDA DONE
 *
 *  Add null checks to prevent crashing         DONE
 *  Change member display to names              DONE
 *  Add cancelled error messages                DONE
 *
 *  Toast messages or Notifications to show member changes? NOPE
 */
public class TimelineSettings extends AppCompatActivity
        implements AddFriendFragment.AddFriendListener, ChangeProfilePicFragment.ChangeProfilePicListener {
    private CircleImageView timelineCircleView; // Picture of the current timeline
    private String currentTimelineID;           // ID of the current timeline
    private String currentTimelineName;         // Name of the current timeline
    private TextView squadTitle;                // Name of timeline
    private TextView addFriend;                 // Add Friend button
    private TextView leaveSquad;                // Leave Squad button
    private ArrayAdapter<String> adapter;       // Adapter for list of users
    private ArrayList<String> users;            // List of user names
    private ArrayList<String> sortedUsers;      // List of user names sorted alphabetically (for display)
    private ArrayList<String> userIDs;          // List of user IDs    (for easy iteration when searching)
    private String lastModified;                // Last modified date
    private String squadImage;                  // String of the picture


    private Firebase db;                        // Database object

    private boolean returnName;                 // Whether or not to pass back timeline name
    private boolean deleted;                    // Whether or not this timeline has been deleted
    private boolean invalid;                    // Whether or not this timeline is valid

    // String constants for Firebase children
    private final String TITLE_STR = "Title";
    private final String DB_USERS = "Users";                // Users table in main DB
    private final String USERS_STR = "Users";               // Users table in Timelines
    private final String TIMELINE_STR = "Timelines";
    private final String PIC_STR = "TimelinePic";
    private final String MOD_STR = "LastModified";
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
    private final String ADD_ALREADY_EXISTS = " is already in this squad.";
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
        timelineCircleView = (CircleImageView) findViewById(R.id.squad_image);
        squadTitle =  (TextView) findViewById(R.id.squad_title);
        addFriend = (TextView) findViewById(R.id.addPeople);
        leaveSquad = (TextView) findViewById(R.id.leaveSquad);
        NonScrollListView user_list = (NonScrollListView) findViewById(R.id.user_list);

        // Set font for the title
        Typeface squadTitleFont = Typeface.createFromAsset(getAssets(), getString(R.string.RobotoMedium));
        squadTitle.setTypeface(squadTitleFont);

        // By default, there is no reason to return the name
        returnName = false;

        // By default, the timeline exists
        deleted = false;

        // By default, the timeline is valid
        invalid = false;

        // Instantiate list of names, IDs, and the adapter to the ListView
        // Assign the name list to the adapter, since that's what we will display
        users = new ArrayList<String>();
        sortedUsers = new ArrayList<String>();
        userIDs = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.settings_user_list, sortedUsers);

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
            // Timeline is invalid
            invalid = true;

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
                // Stop data retrieval
                if (dataSnapshot.child(TITLE_STR).getValue() == null) {
                    // Timeline is invalid
                    invalid = true;

                    // Perform no further actions with data
                    return;
                }

                // Get value of the title child of timeline and update title
                squadTitle.setText(dataSnapshot.child(TITLE_STR).getValue().toString());

                // Get last modified date and image of squad, and update variables
                lastModified = dataSnapshot.child(MOD_STR).getValue().toString();
                squadImage = dataSnapshot.child(PIC_STR).getValue().toString();

                // If the timeline has a picture, then convert to Bitmap and display it
                // otherwise, the default picture will display (coded in the xml file)
                if(!squadImage.isEmpty()) {
                    Bitmap bm_image = PictureCompactor.StringB64ToBitmap(squadImage);
                    timelineCircleView.setImageBitmap(bm_image);
                }

                // Reset the list of user names and IDs
                users.clear();
                sortedUsers.clear();
                userIDs.clear();

                // Iterate through the users in the table
                // and add their names and IDs to the lists of timeline users
                for (DataSnapshot member : dataSnapshot.child(USERS_STR).getChildren()) {
                    users.add(member.getValue().toString());
                    sortedUsers.add(member.getValue().toString());
                    userIDs.add(member.getKey());
                }

                // We are sorting a separate list of names so that
                // the original list of users and the list of IDs
                // retain their matching indices
                // Sort the list of names while ignoring case
                Collections.sort(sortedUsers, String.CASE_INSENSITIVE_ORDER);

                // Notify the adapter of the data change and refresh the view
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // If load was cancelled, informed user of error
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

    // Listener for Add Friend dialog
    // Check for invalid email format
    // nonexistent email
    // already in timeline
    // if not, add and display success

    // Add Friend Positive Button Listener interface method
    @Override
    public void onDialogPositiveClick(final AddFriendFragment dialog) {
        // Get the email entered in the dialog
        final String email = dialog.getEmail();

        // Verify that it is actually an email address format
        // Evaluates to true if there is no @, if there is nothing between the period and the @
        // or if there is nothing after the period
        if (email.indexOf('@') < 0 || email.lastIndexOf('.') < email.indexOf('@') + 2
                || email.lastIndexOf('.') > email.length() - 2) {
            // Print out the Toast email error message
            showEmailErrorMsg();

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

                    // Get the stored email of the user to retain consistency in style
                    // Entered email may have style inconsistencies with stored email
                    // that may cause problems later on
                    String userEmail = user.child(EMAIL_STR).getValue().toString();

                    // Get the user ID
                    String userID = user.getKey();

                    // Get the first name of the user and append the last name to save full name
                    // The first name is stored for later use
                    String userFirstName = user.child(FIRST_NAME).getValue().toString();
                    String userName = userFirstName + " " + user.child(LAST_NAME).getValue().toString();

                    // If the emails match (emails are case insensitive)
                    if (userEmail.equalsIgnoreCase(email)) {
                        //If the user is already part of this timeline, notify user and end method
                        if(user.child(TIMELINE_STR + "/" + currentTimelineID).exists()) {
                            showAddAlreadyExistsMsg(userEmail);
                            return;
                        }

                        // Get table of user's timelines and add all required information
                        Firebase userTimelineTable = Vars.getUser(userID).child(TIMELINE_STR);
                        Map<String, Object> userTimelines = new HashMap<String, Object>();
                        Map<String, String> timelineData = new HashMap<String, String>();
                        timelineData.put(TITLE_STR, currentTimelineName);
                        timelineData.put(MOD_STR, lastModified);

                        // Get names of all users in the current timeline
                        for(int index = 0; index < users.size(); index++) {
                            String temp = users.get(index);
                            temp = temp.substring(0, temp.indexOf(" ")); //parses to get first name
                            timelineData.put(userIDs.get(index), temp);
                        }

                        // Store the timeline data in added user's timeline table
                        timelineData.put(userID, userFirstName);
                        userTimelines.put(currentTimelineID, timelineData);
                        userTimelineTable.updateChildren(userTimelines);

                        // New HashMap containing user information to store in the Timeline
                        HashMap<String, Object> newUser = new HashMap<String, Object>();
                        newUser.put(userID, userFirstName);

                        // Add new user to all other users' timeline table
                        for(String member : userIDs) {
                            Vars.getUser(member).child(TIMELINE_STR + "/" + currentTimelineID)
                                    .updateChildren(newUser);
                        }

                        // Add the user's ID and email and put it in the Timeline's Users table
                        newUser = new HashMap<String, Object>();
                        newUser.put(userID, userName);
                        Vars.getTimeline(currentTimelineID).child(USERS_STR).updateChildren(newUser);

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

    // Change squad profile pic
    @Override
    public void onDialogPositiveClick(final ChangeProfilePicFragment dialog) {

        String photo = dialog.getPhoto();
        if (photo.length() == 0) {
            Toast.makeText(getApplicationContext(), "ERROR: The photo you have chosen is not valid. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            timelineCircleView.setImageBitmap(PictureCompactor.StringB64ToBitmap(photo));
        }

        Vars.getFirebase().child(TIMELINE_STR + "/" + currentTimelineID + "/" + PIC_STR).setValue(photo);

        dialog.getDialog().dismiss();

        String snackMessage = "The squad photo has been changed.";
        Snackbar.make(findViewById(android.R.id.content), snackMessage, Snackbar.LENGTH_LONG).show();

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

                        //Iterate through all users in the timeline
                        for(int index = 0; index < userIDs.size(); index++) {
                            // Update users who aren't the current user
                            if(!userIDs.get(index).equals(Vars.getUID())) {
                                // Remove the current user from their local timelines
                                Vars.getUser(userIDs.get(index)).child(TIMELINE_STR + "/" + currentTimelineID)
                                        .updateChildren(removedUser);
                            }
                        }

                        // If the current user was the only member
                        // the timeline will be automatically deleted
                        if (users.size() == 1) {
                            // Set the deleted flag to true to prevent
                            // Toast data error messages from appearing
                            deleted = true;

                            // Set the value of the timeline in the database to null
                            // effectively deleting the timeline
                            Vars.getTimeline(currentTimelineID).setValue(null);
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
        // We don't want the user to change the name while viewing an invalid timeline
        // So just do nothing if the user presses the button
        if (invalid) {
            return;
        }

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
                        String newName = squadNameInput.getText().toString();
                        // If the same name or empty name was entered, then take no action
                        if (newName.equals(currentTimelineName) || newName.equals("")) {
                            return;
                        }

                        // Set the current timeline name to the entered one
                        // Both locally and in the database
                        currentTimelineName = newName;
                        Vars.getTimeline(currentTimelineID).child(TITLE_STR).setValue(currentTimelineName);

                        // Iterate through the list of the current timeline users
                        // We have to correct the timeline names on the user side as well
                        for(String uid : userIDs) {
                            Vars.getUser(uid).child(TIMELINE_STR + "/" + currentTimelineID + "/" + TITLE_STR).setValue(currentTimelineName);
                        }

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

    // Trevor's Stuff - Options Menu
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
            case R.id.changeSquadPhoto:
                // Don't let the user change the profile picture of an invalid timeline
                if (invalid) {
                    return true;
                }
                // Show Change Profile Pic dialog
                DialogFragment dialog = new ChangeProfilePicFragment();
                dialog.show(getSupportFragmentManager(), "ChangeProfilePicFragment");
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