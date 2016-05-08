package com.keepingatimeline.kat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by boredguy88 on 5/5/2016.
 */
public class Timelineshower extends AppCompatActivity {

    private Firebase db;
    private ArrayList<String> holder = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timelineshower);
        Firebase.setAndroidContext(this);
        db = new Firebase("https://fiery-fire-8218.firebaseio.com/Users/swag/Timelines");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot time: dataSnapshot.getChildren()){
                    holder.add((String) time.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ArrayAdapter<String> timelineAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, holder);
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(timelineAdapter);

    }

}
