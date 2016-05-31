package com.keepingatimeline.kat;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
* Created by poopyfeet on 5/30/16.
*/
public class ChangePasswordFragment extends DialogFragment {
    private Activity activityRef;
    private AlertDialog dialog;                 // Dialog to display
    private ChangePasswordListener pListener;
    private EditText curPass;
    private EditText newPass;
    private EditText conPass;
    private String emailStr;
    // Define an interface that positive button listeners must implement
    public interface ChangePasswordListener {
        // Positive button listener onClick
        void onDialogPositiveClick(ChangePasswordFragment dialog);
    }

    public String getCurPass() {
        return curPass.getText().toString();
    }

    public String getNewPass() {
        return newPass.getText().toString();
    }

    public String getConPass() {
        return conPass.getText().toString();
    }

    @Override
    public void onAttach(Activity activity) {
        // Call super method
        super.onAttach(activity);
        activityRef = activity;
        emailStr = Vars.getFirebase().getAuth().getProviderData().get("email").toString();
         //Try to set activity as positive button listener
        try {
            pListener = (ChangePasswordListener)activity;
        }
        // If this fails, it means that activity must implement the listener interface
        catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + "must implement ChangePasswordListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a dialog builder and layout inflater, and inflate the view
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);

        curPass = (EditText) view.findViewById(R.id.currentPasswordInput);
        newPass = (EditText) view.findViewById(R.id.newPasswordInput);
        conPass = (EditText) view.findViewById(R.id.confirmPasswordInput);

        builder.setTitle("Change Password");
        builder.setMessage("Enter your current password, then enter a new password and confirm it.");

        // Set the dialog view to the inflated xml
        builder.setView(view);


        // Set positive button's text to Add
        // Don't do anything because this will be overridden
        builder.setPositiveButton("Save", null);
        // Set negative button's text to Cancel
        builder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog when button is pressed
                ChangePasswordFragment.this.getDialog().cancel();
            }
        });

        // Create the dialog
        dialog = builder.create();

        // Set a new onShowListener to override positive button's listener
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // Get the positive button of the dialog
                Button submit = ChangePasswordFragment.this.dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                // We override the original listener method because we do not want to exit the dialog
                // until we verify that the entered email is valid
                submit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Call positive button listener's on click method
                        // and pass in current dialog fragment
                        pListener.onDialogPositiveClick(ChangePasswordFragment.this);

                        // Don't dismiss
                        // Listener will dismiss when data is validated
                    }
                });
            }
        });



        return dialog;

    } // end onCreateDialog

}
