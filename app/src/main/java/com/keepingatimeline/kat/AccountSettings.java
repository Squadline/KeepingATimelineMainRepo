package com.keepingatimeline.kat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * By:
 *      Byung
 * Description:
 *      This code creates a list of settings using the ListView class.
 **/
public class AccountSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        //initializes the list of settings to go into the ListView class.
        String[] settings = {"Change Username", "Change Password"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,settings);
        ListView myList = (ListView) findViewById(R.id.listview);

        //same concept as the code inside the MainScreen, except here you go through the string array
        //to fetch the corresponding setting which will take me to a new instance
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch( position ) {
                    case 0:
                        Intent newActivity = new Intent("com.keepingatimeline.kat.Setting1");
                        startActivity(newActivity);
                        break;
                    case 1:
                        Intent newActivity1 = new Intent("com.keepingatimeline.kat.ChangePassword");
                        startActivity(newActivity1);
                        break;
                }
            }
        });

    }
}
