package com.keepingatimeline.kat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private TextView username;
    private TextView password;
    private Firebase fRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        fRef = Vars.getFirebase();

        if (fRef.getAuth() != null)
        {
            Intent newActivity = new Intent("com.keepingatimeline.kat.MainScreen");
            startActivity(newActivity);
            // END THIS ACTIVITY
            this.finish();
            return;
        }


        Button btnLogin = (Button) findViewById(R.id.loginButton);
        TextView signUpText = (TextView) findViewById(R.id.signUpText);
        TextView forgotPass = (TextView) findViewById(R.id.forgotPassword);
        username = (TextView) findViewById(R.id.loginEmail);
        password = (TextView) findViewById(R.id.loginPassword);

        final AppCompatActivity me = this;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fRef.authWithPassword(username.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        Intent newActivity = new Intent("com.keepingatimeline.kat.MainScreen");
                        startActivity(newActivity);
                        me.finish();
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        Log.d("Invalid:", "username/password combination");

                        //consider just doing "Error: "firebaseError.getMessage()?
                        Toast.makeText(getApplicationContext(), "ERROR: The email address or password you entered is not valid. Please try again."
                                , Toast.LENGTH_LONG).show();
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
            public void onClick(View view) {
                Intent registrationActivity = new Intent("com.keepingatimeline.kat.RegistrationScreen");
                startActivity(registrationActivity);
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

        dialogBuilder = new AlertDialog.Builder(this);
        Firebase.setAndroidContext(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reset_password, null);

        final EditText emailInput = (EditText) view.findViewById(R.id.resetPasswordInput);

        dialogBuilder.setTitle("Reset Password");
        dialogBuilder.setMessage("Enter your email address and we'll send you a link to reset your password.");
        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com");
                String email_address = emailInput.getText().toString();
                ref.resetPassword(email_address, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {

                        //Toast.makeText(getApplicationContext(), "Email has been sent.", Toast.LENGTH_LONG).show();

                        Snackbar.make(findViewById(android.R.id.content), "Email has been sent.", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {

                        Toast.makeText(getApplicationContext(), "ERROR: There is no user registered with that email address."
                                , Toast.LENGTH_LONG).show();
                    }
                });
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
