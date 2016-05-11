package com.keepingatimeline.kat;


import android.content.Intent;
import android.net.Credentials;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


import java.util.Map;

public class RegistrationScreen extends AppCompatActivity {
    private EditText name;
    private EditText password1;
    private EditText password2;
    private EditText emailAdd;
    private EditText name1st;
    private EditText name2nd;

    private Button CLR;
    private Button submitUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        Firebase.setAndroidContext(this);

        name = (EditText) findViewById(R.id.username_input);
        password1 = (EditText) findViewById(R.id.password_input);
        password2 = (EditText) findViewById(R.id.password2_input);
        emailAdd = (EditText) findViewById(R.id.email_input);
        name1st = (EditText) findViewById(R.id.firstname_input);
        name2nd = (EditText) findViewById(R.id.lastname_input);

        CLR = (Button) findViewById(R.id.reset_button);
        submitUp = (Button) findViewById(R.id.submit_button);

        CLR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                password1.setText("");
                password2.setText("");
                emailAdd.setText("");
                name1st.setText("");
                name2nd.setText("");
                name.requestFocus();
            }
        });

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

    }

}
