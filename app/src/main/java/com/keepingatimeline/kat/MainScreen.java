package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
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

import com.batch.android.Batch;
import com.batch.android.BatchPushService;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ChangePasswordFragment.ChangePasswordListener, ChangeProfilePicFragment.ChangeProfilePicListener {

    private Firebase database;
    private ArrayList<Timeline> timelines = new ArrayList<>();

    private TimelineAdapter inflateTimeline;
    private ListView timelineList;
    private String holder;
    private String newName;
    private TextView titleBar;
    private String nameAdd;

    private String currentFirst;                // First name of the current user
    private String currentLast;                 // Last name of the current user
    private String currentEmail;                // Email of the current user
    private String currentPic;                  // profile pic in str form

    // String constants for Firebase children
    private final String USERS_STR = "Users";
    private final String NAME_STR = "FirstName";
    private final String LAST_STR = "LastName";
    private final String EMAIL_STR = "EmailAddress";
    private final String PROFILE_STR = "ProfilePic";
    private final String DB_STR = "https://fiery-fire-8218.firebaseio.com/";
    private TextView userName;
    private TextView userEmail;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CircleImageView profilePic;

    /**
     * By: Dana, Byung, Jimmy, Trevor
     * Description:
     * Contains various snippets of code that implement the following functionality:
     * Left Scrollbar (account settings and such)
     * Right Scrollbar (to be moved when timeline function becomes fully functional)
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
        Typeface mainTitleFont = Typeface.createFromAsset(getAssets(), getString(R.string.BebasNeueBold));
        titleBar.setTypeface(mainTitleFont);

        // Opens sidebar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // sets name and email of current user in sidebar
        View navHeader = navigationView.getHeaderView(0);
        userName = (TextView) navHeader.findViewById(R.id.user_name);
        userEmail = (TextView) navHeader.findViewById(R.id.user_email);
        profilePic = (CircleImageView) navHeader.findViewById(R.id.profile_image);
        //.com/Users/uid
        //.com/Users/

        Firebase current = ref.child(USERS_STR).child(holder);

        current.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the name of  the current user
                currentPic = dataSnapshot.child(PROFILE_STR).getValue().toString();
                currentFirst = dataSnapshot.child(NAME_STR).getValue().toString();
                currentLast = dataSnapshot.child(LAST_STR).getValue().toString();

                // Prevents null object reference
                currentFirst = currentFirst + "";
                currentLast = currentLast + "";

                String fullName = currentFirst + " " + currentLast;

                userName.setText(fullName);

                // Get the email of the current user
                currentEmail = dataSnapshot.child(EMAIL_STR).getValue().toString();
                currentEmail = currentEmail + "";

                userEmail.setText(currentEmail);

                if (currentPic.length() != 0) {
                    profilePic.setImageBitmap(PictureCompactor.StringB64ToBitmap(currentPic));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Error loading data.",
                        Toast.LENGTH_SHORT).show();
            }
        });


        inflateTimeline = new TimelineAdapter(this, timelines);
        timelineList = (ListView) findViewById(R.id.timelineList);
        timelineList.setAdapter(inflateTimeline);

        // moved this from onStart() --Dana
        //set url reference to active user
        database = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder + "/Timelines");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DB_Load", "Asking Firebase for data.");

                // All of the stuff that doesn't make sense was written by Trevor

                timelines.clear();

                for (DataSnapshot tlSnapshot : dataSnapshot.getChildren()) {

                    Timeline newTimeline = new Timeline();
                    newTimeline.setId(tlSnapshot.getKey());
                    newTimeline.setTitle(tlSnapshot.child("Title").getValue().toString());
                    newTimeline.setLastmodified(tlSnapshot.child("LastModified").getValue().toString());

                    ArrayList<String> otherUsers = new ArrayList<String>();

                    for (DataSnapshot entry : tlSnapshot.getChildren()) {
                        if (!entry.getKey().toString().equals("Title") &&
                                !entry.getKey().toString().equals("LastModified")) {
                            otherUsers.add(entry.getValue().toString());
                        }
                    }

                    Collections.sort(otherUsers, String.CASE_INSENSITIVE_ORDER);

                    String members = "";

                    for (int index = 0; index < otherUsers.size(); index++) {
                        members += otherUsers.get(index);
                        if (index < otherUsers.size() - 2) members += ", ";
                        else if (index == otherUsers.size() - 2) members += " & ";
                    }

                    newTimeline.setMembers(members);

                    timelines.add(newTimeline);
                }
                mergeSort(timelines);

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
                String timelineName = timelines.get(position).getTitle();

                Intent viewTimelineActivity = new Intent(MainScreen.this, ViewTimeline.class);
                viewTimelineActivity.putExtra("Timeline Name", timelineName);
                viewTimelineActivity.putExtra("Timeline ID", timelines.get(position).getId());
                startActivity(viewTimelineActivity);
            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        Batch.Push.setSmallIconResourceId(R.drawable.notification_icon);
        Batch.Push.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_launcher));
        Batch.onStart(this);
        Batch.User.editor().setIdentifier(holder).save();
        inflateTimeline.notifyDataSetChanged(); //updates adapter --Dana
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.keepingatimeline.kat/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    // Batch stuff - Darren
    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.keepingatimeline.kat/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        Batch.onStop(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Batch.onDestroy(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Batch.onNewIntent(this, intent);
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
                        ref = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String todaysDate = DateGen.getCurrentDate();
                                nameAdd = dataSnapshot.child("FirstName").getValue().toString() + " ";
                                nameAdd += dataSnapshot.child("LastName").getValue().toString();
                                newName = nameTLInput.getText().toString();
                                database = Vars.getFirebase().child("Timelines");
                                database = database.push();
                                String tKey = database.getKey();

                                Map<String, String> timeline = new HashMap<String, String>();
                                timeline.put("Title", newName);
                                timeline.put("LastModified", todaysDate);
                                timeline.put("TimelinePic", "");
                                database.setValue(timeline);

                                Firebase timelineUsers = database.child("Users");
                                Map<String, String> post1 = new HashMap<String, String>();
                                post1.put(Vars.getUID(), nameAdd);
                                timelineUsers.setValue(post1);

                                database = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/" + holder + "/Timelines");
                                database = database.child(tKey);
                                Map<String, Object> userTimelines = new HashMap<String, Object>();
                                userTimelines.put("Title", newName);
                                userTimelines.put("LastModified", todaysDate);
                                userTimelines.put(Vars.getUID(), dataSnapshot.child("FirstName").getValue().toString());
                                database.updateChildren(userTimelines);
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
            showChangeNameDialog();
        } else if (id == R.id.navChangePassword) {
            DialogFragment dialog = new ChangePasswordFragment();
            dialog.show(getSupportFragmentManager(), "ChangePasswordFragment");

        } else if (id == R.id.navChangePhoto) {
            DialogFragment dialog = new ChangeProfilePicFragment();
            dialog.show(getSupportFragmentManager(), "ChangeProfilePicFragment");

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

    @Override
    public void onDialogPositiveClick(final ChangePasswordFragment dialog) {
        final String emailStr = Vars.getFirebase().getAuth().getProviderData().get("email").toString();
        String curPass = dialog.getCurPass();
        String newPass = dialog.getNewPass();
        String conPass = dialog.getConPass();

        // FORM CHECKING

        // check if anything is empty
        if (newPass.length() == 0 ||
                curPass.length() == 0 ||
                conPass.length() == 0) {

        }
        // check if new pass == confirm pass
        if (!newPass.equals(conPass)) {

            Toast.makeText(getApplicationContext(), "New passwords do not match! :0",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // check check if currpass == new pass, aka no point

        if (newPass.equals(curPass)) {
            Toast.makeText(getApplicationContext(), "Current password same as new password!"
                            + "Please change the password to something different!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // SERVER CHECKING
        Vars.getFirebase().changePassword(emailStr, curPass,
                newPass, new Firebase.ResultHandler() {
                    @Override
                    public void onError(FirebaseError err) {
                        if (err.getCode() == FirebaseError.INVALID_PASSWORD) {
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
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Password successfully changed",
                                Toast.LENGTH_LONG).show();
                        dialog.getDialog().cancel();
                        return;
                    }

                });
    }

    private void showChangeNameDialog() {
        // Create the dialog builder and layout inflater
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the dialog xml
        // Parent is null because it is a dialog layout
        View view = inflater.inflate(R.layout.dialog_change_name, null);

        // Get the EditText field in the dialog and preset it to current user name
        final EditText firstNameInput = (EditText) view.findViewById(R.id.firstNameInput);
        final EditText lastNameInput = (EditText) view.findViewById(R.id.lastNameInput);

        // Set the view, title, message, and buttons for the dialog
        builder.setView(view)
                .setTitle("Change Name")
                .setMessage("change stuff cuz can")
                // If the user confirms the title change
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final Firebase database = Vars.getCurrentUser();

                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String newFirstName = firstNameInput.getText().toString();
                                String newLastName = lastNameInput.getText().toString();
                                String currentFirstName = dataSnapshot.child("FirstName").getValue().toString();
                                String currentLastName = dataSnapshot.child("LastName").getValue().toString();
                                // If the same name was entered, then take no action
                                if (newFirstName.equals(currentFirstName) && newLastName.equals(currentLastName)) {
                                    return;
                                }

                                //update Account Sidebar
                                userName.setText(newFirstName + " " + newLastName);

                                // Set the current user name to the entered one
                                // Both locally and in the database
                                database.child("FirstName").setValue(newFirstName);
                                database.child("LastName").setValue(newLastName);

                                for (DataSnapshot timeline : dataSnapshot.child("Timelines").getChildren()) {
                                    Vars.getTimeline(timeline.getKey()).child(Vars.getUID()).setValue(newFirstName + " " + newLastName);
                                    if (!newFirstName.equals(currentFirstName)) {
                                        for (DataSnapshot user : timeline.getChildren()) {
                                            String path = "Timelines/" + timeline.getKey() + "/" + user.getKey();
                                            Vars.getUser(user.getKey()).child(path).setValue(newFirstName);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

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


    // saves data for editing profile picture
    @Override
    public void onDialogPositiveClick(final ChangeProfilePicFragment dialog) {
        String uid = Vars.getFirebase().getAuth().getUid();
        String photo = dialog.getPhoto();
        if (photo.length() == 0) {
            Toast.makeText(getApplicationContext(), "No Photo Entered!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            profilePic.setImageBitmap(PictureCompactor.StringB64ToBitmap(photo));
        }
        Vars.getFirebase().child("Users/" + uid + "/ProfilePic").setValue(photo);
        Toast.makeText(getApplicationContext(), "Profile Picture Saved!", Toast.LENGTH_SHORT).show();

        dialog.getDialog().cancel();
    }

    // displays a help dialogue --Dana
    private void getMainScreenHelp() {
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

    private ArrayList<Timeline> mergeSort(ArrayList<Timeline> whole) {
        ArrayList<Timeline> left = new ArrayList<Timeline>();
        ArrayList<Timeline> right = new ArrayList<Timeline>();
        int center;

        if (whole.size() <= 1) {
            return whole;
        } else {
            center = whole.size()/2;
            // copy the left half of whole into the left.
            for (int i=0; i<center; i++) {
                left.add(whole.get(i));
            }

            //copy the right half of whole into the new arraylist.
            for (int i=center; i<whole.size(); i++) {
                right.add(whole.get(i));
            }

            // Sort the left and right halves of the arraylist.
            left  = mergeSort(left);
            right = mergeSort(right);

            // Merge the results back together.
            merge(left, right, whole);
        }
        return whole;
    }

    private void merge(ArrayList<Timeline> left, ArrayList<Timeline> right, ArrayList<Timeline> whole) {
        int leftIndex = 0;
        int rightIndex = 0;
        int wholeIndex = 0;

        // As long as neither the left nor the right ArrayList has
        // been used up, keep taking the smaller of left.get(leftIndex)
        // or right.get(rightIndex) and adding it at both.get(bothIndex).
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if ( !DateGen.compareDates(left.get(leftIndex).getLastmodified(),
                    right.get(rightIndex).getLastmodified())) {
                whole.set(wholeIndex, left.get(leftIndex));
                leftIndex++;
            } else {
                whole.set(wholeIndex, right.get(rightIndex));
                rightIndex++;
            }
            wholeIndex++;
        }

        ArrayList<Timeline> rest;
        int restIndex;
        if (leftIndex >= left.size()) {
            // The left ArrayList has been used up...
            rest = right;
            restIndex = rightIndex;
        } else {
            // The right ArrayList has been used up...
            rest = left;
            restIndex = leftIndex;
        }

        // Copy the rest of whichever ArrayList (left or right) was not used up.
        for (int i=restIndex; i<rest.size(); i++) {
            whole.set(wholeIndex, rest.get(i));
            wholeIndex++;
        }
    }
}
