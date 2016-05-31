package com.keepingatimeline.kat;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

/**
 * Created by Darren on 5/22/2016.
 *
 * Class: AddFriendFragment
 * Purpose: Fragment for Add Friend dialog in Timeline Settings
 */
public class AddFriendFragment extends DialogFragment {

    private AlertDialog dialog;                 // Dialog to display
    private AddFriendListener mListener;        // Listener for positive button clicks
    private EditText addFriendInput;            // Text field for email input
    private String email;                       // Email entered

    // Define an interface that p ositive button listeners must implement
    public interface AddFriendListener {
        // Positive button listener onClick
        void onDialogPositiveClick(AddFriendFragment dialog);
    }

    // Get the EditText and return entered text
    public String getEmail() {
        email = addFriendInput.getText().toString();
        return email;
    }

    @Override
    public void onAttach(Activity activity) {
        // Call super method
        super.onAttach(activity);

        // Try to set activity as positive button listener
        try {
            mListener = (AddFriendListener)activity;
        }
        // If this fails, it means that activity must implement the listener interface
        catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + "must implement AddFriendListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a dialog builder and layout inflater, and inflate the view
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_friend, null);

        addFriendInput = (EditText) view.findViewById(R.id.addFriendInput);

        builder.setTitle("Add Friend");
        builder.setMessage("Enter the email address of the user that you want to add to the timeline.");

        // Set the dialog view to the inflated xml
        builder.setView(view)
                // Set positive button's text to Add
                // Don't do anything because this will be overridden
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing (overridden method)
                    }
                })
                // Set negative button's text to Cancel
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the dialog when button is pressed
                        AddFriendFragment.this.getDialog().cancel();
                    }
                });

        // Create the dialog
        dialog = builder.create();

        // Set a new onShowListener to override positive button's listener
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // Get the positive button of the dialog
                Button add = AddFriendFragment.this.dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                // We override the original listener method because we do not want to exit the dialog
                // until we verify that the entered email is valid
                add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Call positive button listener's on click method
                        // and pass in current dialog fragment
                        mListener.onDialogPositiveClick(AddFriendFragment.this);

                        // Don't dismiss
                        // Listener will dismiss when data is validated
                    }
                });
            }
        });

        // Return the created dialog
        return dialog;
    }
}
