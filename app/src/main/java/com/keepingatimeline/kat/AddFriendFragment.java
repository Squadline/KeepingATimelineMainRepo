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
import android.widget.TextView;

/**
 * Created by Darren on 5/22/2016.
 */
public class AddFriendFragment extends DialogFragment {

    private AlertDialog dialog;
    private AddFriendListener mListener;
    private String email;

    public interface AddFriendListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    public String getEmail() {
        email = ((TextView) dialog.findViewById(R.id.addFriendInput)).getText().toString();
        return email;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        System.err.println("Reached attach");

        try {
            mListener = (AddFriendListener)activity;
        }
        catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + "must implement AddFriendListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_addfriend, null))
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add friend
                        mListener.onDialogPositiveClick(AddFriendFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel the dialog
                        AddFriendFragment.this.getDialog().cancel();
                    }
                });
        dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button pb = AddFriendFragment.this.dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                pb.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

        System.err.println("Reached create");
        return dialog;
    }
}
