package com.keepingatimeline.kat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/* Written by: Louis Leung
   Main login screen, this is what launches on start
 */
public class LoginActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button)findViewById(R.id.loginButton);
        TextView signUpText = (TextView)findViewById(R.id.signUpText);
        TextView forgotPass = (TextView) findViewById(R.id.forgotPassword);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent("com.keepingatimeline.kat.MainScreen");
                startActivity(newActivity);
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

        final EditText emailInput = new EditText(this);
        dialogBuilder = new AlertDialog.Builder(this);
        Firebase.setAndroidContext(this);

        dialogBuilder.setTitle("Reset Password");
        dialogBuilder.setMessage("Enter your email address and we'll send you a link to reset your password.");
        dialogBuilder.setView(emailInput);
        dialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Firebase ref = new Firebase("https://fiery-fire-8218.firebaseio.com");
                String email_address = emailInput.getText().toString();
                ref.resetPassword(email_address, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {

                        Toast.makeText(getApplicationContext(), "Email has been sent.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {

                        Toast.makeText(getApplicationContext(), "Oops! It seems like there are some error!"
                                , Toast.LENGTH_SHORT).show();
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
