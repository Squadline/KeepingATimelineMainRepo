package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jimmy on 5/5/2016.
 */
public class Timelineshower extends AppCompatActivity {

    private Firebase db;
    private ArrayList<String> holder = new ArrayList<String>();
    private ArrayList<String> small = new ArrayList<String>();
    private Firebase db2;
    private Firebase db3;
    private Button b;
    private TimelineAdapter plz;
    private ListView list;
    private Button c;
    private Context context;

    ImageButton floatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelineshower_front);

        /*
        c = (Button) findViewById(R.id.button5);
        c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "heyyyyy ;)", Toast.LENGTH_LONG).show();
            }
        });
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.fab_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case R.id.timeline_d:
                new AlertDialog.Builder(this).setTitle("Delete Timeline")
                        .setMessage("Are you sure you want to delete this Timeline? :(")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //delete the timeline here
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //go back
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            case R.id.timeline_u:

                        //update timeline dialog

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_timelineshower_front);
        Firebase.setAndroidContext(this);
        db = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/swag/Timelines");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot time: dataSnapshot.getChildren()){
                    holder.add((String) time.getKey());
                    small.add("Admin: " + (String) time.getValue());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        floatButton = (ImageButton) findViewById(R.id.fab);
        floatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "heyyyyy ;)", Toast.LENGTH_LONG).show();
            }
        });



        //ArrayAdapter<String> timelineAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, holder);
        //ListView list = (ListView) findViewById(R.id.listView);
        //list.setAdapter(timelineAdapter);

        b = (Button) findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent("com.keepingatimeline.kat.MainScreen");
                startActivity(newActivity);

              //SHOULD ALWAYS USE UPDATE CHILDREN so you don't overwrite, used "put" as an example
              // updateChildren does not require you to make a seperate map reference, can just store by key value pair
              //add timelines to user portion of db, use update children to retaiin data
              db2 = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/swag/Timelines");
              Map<String, String> t = new HashMap<String, String>();
              t.put("name of timeline", "admin/author");
              db2.setValue(t);

              //add timelines to timeline portion of db, should be using update children to retain data
              db3 = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines");
              Firebase newRef = db3.push();     //generated new unique id
              Map<String, String> t2 = new HashMap<String, String>();
              t2.put("Events", "added in a different section");
              t2.put("admin", "whoever the admin is");
              t2.put("name", "name of timeline");
              t2.put("privacy", "set privacy level");
              newRef.setValue(t2);          //set all the above data to the unique id

              //get the unique id, can also search into unique id and pull name to search
              String id = newRef.getKey();

              /*have to go down one level lower in db, the unique can be grabbed through parsing and
              then concat into the url with a '+', can also be called through the child method */
              Firebase dbx = newRef.child("users"); //
              Map<String, String> tx = new HashMap<String, String>();
              tx.put("some user", "indication they are in, should always be true");
              dbx.setValue(tx);
        }
        });

        TimelineAdapter plz = new TimelineAdapter(holder,small,this);
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(plz);

    }
}
