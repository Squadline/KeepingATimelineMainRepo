package com.keepingatimeline.kat;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Credentials;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


import java.util.Map;

public class RegistrationScreen extends AppCompatActivity {
    private EditText password1;
    private EditText emailAdd;
    private EditText name1st;
    private EditText name2nd;
    private Button submitUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        Firebase.setAndroidContext(this);

        password1 = (EditText) findViewById(R.id.password_input);
        emailAdd = (EditText) findViewById(R.id.email_input);
        name1st = (EditText) findViewById(R.id.firstname_input);
        name2nd = (EditText) findViewById(R.id.lastname_input);
        submitUp = (Button) findViewById(R.id.submit_button);

        submitUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com");
                String user = emailAdd.getText().toString();
                String password = password1.getText().toString();
                ref.createUser(user, password, new Firebase.ValueResultHandler<Map<String, Object>>()
                {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });
                finish();
            }
        });

        TextView signInText = (TextView)findViewById(R.id.signInText);

        SpannableString signInString = new SpannableString("Already have an account? Sign In.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View signInText) {
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        signInString.setSpan(clickableSpan, 25, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signInString.setSpan(new StyleSpan(Typeface.BOLD), 25, 33, 0);
        signInString.setSpan(new ForegroundColorSpan(Color.WHITE), 25, 33, 0);
        signInText.setMovementMethod(LinkMovementMethod.getInstance());
        signInText.setHighlightColor(Color.TRANSPARENT);
        signInText.setText(signInString, TextView.BufferType.SPANNABLE);

    }
}
