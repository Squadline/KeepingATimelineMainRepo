package com.keepingatimeline.kat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/* Written by: Louis Leung
   Main login screen, this is what launches on start
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button)findViewById(R.id.loginButton);
        TextView signUpText = (TextView)findViewById(R.id.signUpText);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent("com.keepingatimeline.kat.MainScreen");
                startActivity(newActivity);
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

        /* for forgot password button, if they click it, goes to Change Password
        Activity
         */
        TextView forgotPass = (TextView) findViewById(R.id.forgotPassword);
        /* assert not null just a formality */
        assert forgotPass != null;
        forgotPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent newActivity = new Intent("com.keepingatimeline.kat.ChangePassword");
                startActivity(newActivity);
            }
        });
    }
}
