package com.keepingatimeline.kat;

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
    private Button sign_up;
    // timeline settings tester button - Darren
    private Button tSettings;

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
                        Intent newActivity = new Intent("com.keepingatimeline.kat.AccountSettings");
                        startActivity(newActivity);
                        break;
                }
            }
        });

        //call the method here.
        t = (TextView)findViewById(R.id.textUser);
        b = (Button)findViewById(R.id.button2);
        inp = (EditText)findViewById(R.id.editText);
        adText = (Button)findViewById(R.id.button3);
        tl = (Button)findViewById(R.id.button5);
        // timeline settings tester button - Darren
        tSettings = (Button) findViewById(R.id.t_settings);

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
                Intent newActivity = new Intent("com.keepingatimeline.kat.Timelineshower");
                startActivity(newActivity);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent("com.keepingatimeline.kat.RegistrationScreen");
                startActivity(newActivity);
            }
        });

        // testing button to view Timeline Settings - Darren
        tSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent("com.keepingatimeline.kat.TimelineSettings");
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
    }
}
