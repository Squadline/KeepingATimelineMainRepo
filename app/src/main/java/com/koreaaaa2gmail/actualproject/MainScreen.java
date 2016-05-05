package com.koreaaaa2gmail.actualproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainScreen extends AppCompatActivity {

    private static Button accSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

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
        OnClickButtonListener();
    }

    /**
     * By:
     *      Byung
     * Description:
     *      The code that responds to clicking a button. When button is clicked, it goes to the new
     *      activity (or screen) and the code below does that.
     **/
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
}
