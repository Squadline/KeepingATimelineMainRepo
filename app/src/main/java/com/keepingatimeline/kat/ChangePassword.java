package com.keepingatimeline.kat;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Firebase.ResultHandler;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    private EditText email;
    private Button submit;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        email = (EditText) findViewById(R.id.email_putin);
        submit = (Button) findViewById(R.id.reset);
        cancel = (Button) findViewById(R.id.cancel);
        Firebase.setAndroidContext(this);


        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com");
                                          String email_address = email.getText().toString();
                                          ref.resetPassword(email_address, new ResultHandler() {
                                              @Override
                                              public void onSuccess() {
                                                  Context context = getApplicationContext();
                                                  CharSequence text = "Email has been sent! Check it out!";
                                                  int duration = Toast.LENGTH_LONG;

                                                  Toast toast = Toast.makeText(context, text, duration);
                                                  toast.show();

                                              }

                                              @Override
                                              public void onError(FirebaseError firebaseError) {
                                                  Context context = getApplicationContext();
                                                  CharSequence text = "Error! Email was not sent!";
                                                  int duration = Toast.LENGTH_LONG;

                                                  Toast toast = Toast.makeText(context, text, duration);
                                                  toast.show();
                                              }
                                          });
                                          // To make the toast appear before the activity finishes
                                          Runnable r = new Runnable() {
                                              @Override
                                              public void run(){
                                                  finish();
                                              }
                                          };
                                          Handler delayer = new Handler();
                                          delayer.postDelayed(r, 2000);

                                      }
                                  }
        );

        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                         finish();
                                      }
                                  }
        );

    }
}
