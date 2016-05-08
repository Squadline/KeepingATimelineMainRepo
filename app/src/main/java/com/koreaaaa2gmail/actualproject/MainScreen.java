package com.koreaaaa2gmail.actualproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainScreen extends AppCompatActivity {

    private static Button accSettings;
    private Firebase db;
    private TextView t;
    private Button b;
    private EditText inp;
    private Button adText;
    private Button tl;

    /**
     * By: Dana
     * Description:
     *      The code to bring up the sidebar thing.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Firebase.setAndroidContext(this);
        db = new Firebase("https://fiery-fire-8218.firebaseio.com/");


        String[] settings = {"Settings", "Log Out"};
        ListView myList = (ListView) findViewById(R.id.left_drawer);
        myList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,settings));
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch( position ) {
                    case 0:
                        Intent newActivity = new Intent("com.koreaaaa2gmail.actualproject.AccountSettings");
                        startActivity(newActivity);
                        break;
                }
            }
        });

        //call the method here.
<<<<<<< HEAD
        OnClickButtonListener();
        t = (TextView)findViewById(R.id.textUser);
        b = (Button)findViewById(R.id.button2);
        inp = (EditText)findViewById(R.id.editText);
        adText = (Button)findViewById(R.id.button3);
        tl = (Button)findViewById(R.id.button5);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new Firebase("https://fiery-fire-8218.firebaseio.com/Users");
                Firebase swagref =  db.child("swag/FirstName");
                swagref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String stuff = dataSnapshot.getValue(String.class);
                        t.setText(stuff);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent("com.koreaaaa2gmail.actualproject.Timelineshower");
                startActivity(newActivity);
            }
        });

        adText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase r = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/swag");
                Firebase d = r.push();
                Map<String, String> post = new HashMap<String, String>();
                post.put("input", inp.getText().toString());
                d.setValue(post);
                r.child("FirstName").setValue(inp.getText().toString());
            }
        });






=======
        //OnClickButtonListener();
>>>>>>> ff1b5502c5ec938546e5f0c637e25389114281d5
    }


    /**
     * By: Byung
     * Description:
     *      The code that responds to clicking a button. When button is clicked, it goes to the new
     *      activity (or screen) and the code below does that.
    public void OnClickButtonListener() {
        //fetches the object that you want it to respond on a click
        accSettings = (Button)findViewById(R.id.button);
        accSettings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Inside the new intent, there is the address. The address is where the new activity lies on.
                        //Fetch it from the corresponding xml file
                        Intent intent = new Intent("com.koreaaaa2gmail.actualproject.AccountSettings");
                        startActivity(intent);
                    }
                }
        );
    }
    */
}
