package com.keepingatimeline.kat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Darren on 5/22/2016.
 */
public class AddFriendFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addfriend, null);

        final EditText addFriendInput = (EditText) view.findViewById(R.id.addFriendInput);

        builder.setTitle("Add Friend");
        builder.setMessage("Enter the email address of the user that you want to add to the timeline.");

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    // Add friend | Use addFriendInput.getText().getString()
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the dialog
                        AddFriendFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}