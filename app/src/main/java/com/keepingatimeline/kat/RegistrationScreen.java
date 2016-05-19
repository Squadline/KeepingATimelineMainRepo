package com.keepingatimeline.kat;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
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
                final String user = emailAdd.getText().toString();
                String password = password1.getText().toString();
                ref.createUser(user, password, new Firebase.ValueResultHandler<Map<String, Object>>()
                {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Firebase userRef = new Firebase("https://fiery-fire-8218.firebaseio.com/Users");
                        Firebase d = userRef.child(result.get("uid").toString());
                        Map<String, String> post = new HashMap<String, String>();
                        Map<String, String> child = new HashMap<String, String>();
                        post.put("FirstName", name1st.getText().toString());
                        post.put("LastName", name2nd.getText().toString());
                        post.put("EmailAddress", user);
                        post.put("Timelines", "0");
                        d.setValue(post);
                        d = d.child("Timelines");
                        child.put("My first squadline", "Me");
                        d.setValue(child);

                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Context context = getApplicationContext();
                        CharSequence text = "ERROR: The information you entered is not valid. Please try again.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        password1.setText("");
                        emailAdd.setText("");
                        name1st.setText("");
                        name2nd.setText("");
                    }
                });
                /*
                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        finish();
                    }
                };
                Handler delayer = new Handler();
                delayer.postDelayed(r, 2000);
                */
            }
        });

        TextView signInText = (TextView)findViewById(R.id.signInText);

        SpannableString signInString = new SpannableString("Already have an account? Sign In.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
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
