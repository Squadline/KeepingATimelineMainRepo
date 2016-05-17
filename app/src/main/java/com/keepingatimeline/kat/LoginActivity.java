package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/* Written by: Louis Leung
   Main login screen, this is what launches on start
 */
public class LoginActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private TextView username;
    private TextView password;
    private Button login;
    private Firebase fRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

        Button btnLogin = (Button)findViewById(R.id.loginButton);
        TextView signUpText = (TextView)findViewById(R.id.signUpText);
        TextView forgotPass = (TextView) findViewById(R.id.forgotPassword);
        username = (TextView) findViewById(R.id.loginEmail);
        password = (TextView) findViewById(R.id.loginPassword);
        login = (Button) findViewById(R.id.loginButton);
        fRef = new Firebase("https://fiery-fire-8218.firebaseio.com/");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fRef.authWithPassword(username.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        Intent newActivity = new Intent("com.keepingatimeline.kat.MainScreen");
                        startActivity(newActivity);
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        Log.d("Invalid:", "username/password combination");
                    }
                });
            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog();
            }
        });

        SpannableString signUpString = new SpannableString("Don't have an account? Sign Up.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View signUpText) {
                Intent newActivity = new Intent("com.keepingatimeline.kat.RegistrationScreen");
                startActivity(newActivity);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        signUpString.setSpan(clickableSpan, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpString.setSpan(new StyleSpan(Typeface.BOLD), 23, 31, 0);
        signUpString.setSpan(new ForegroundColorSpan(Color.WHITE), 23, 31, 0);
        signUpText.setMovementMethod(LinkMovementMethod.getInstance());
        signUpText.setHighlightColor(Color.TRANSPARENT);
        signUpText.setText(signUpString, TextView.BufferType.SPANNABLE);
    }

    private void forgotPasswordDialog() {

        EditText emailInput = new EditText(this);
        dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Reset Password");
        dialogBuilder.setMessage("Enter your email address and we'll send you a link to reset your password.");
        dialogBuilder.setView(emailInput);
        dialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
           @Override
            public void onClick(DialogInterface dialog, int which) {
               Toast.makeText(getApplicationContext(), "Email has been sent.", Toast.LENGTH_SHORT).show();
           }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialogForgotPassword = dialogBuilder.create();
        dialogForgotPassword.show();
    }
}
