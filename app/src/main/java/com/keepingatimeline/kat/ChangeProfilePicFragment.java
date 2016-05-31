package com.keepingatimeline.kat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by poopyfeet on 5/30/16.
 */
public class ChangeProfilePicFragment extends DialogFragment{



    private static final int RESULT_LOAD_IMAGE = 1;

    TextView uploadPhotoInput;
    private String imagePath;
    private Activity activityRef;
    private AlertDialog dialog;                 // Dialog to display
    private ChangeProfilePicListener pListener;

    // Define an interface that positive button listeners must implement
    public interface ChangeProfilePicListener {
        // Positive button listener onClick
        void onDialogPositiveClick(ChangeProfilePicFragment dialog);
    }


    @Override
    public void onAttach(Activity activity) {
        // Call super method
        super.onAttach(activity);
        activityRef = activity;

        //Try to set activity as positive button listener
        try {
            pListener = (ChangeProfilePicListener) activity;
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
        View view = inflater.inflate(R.layout.dialog_change_profile_picture, null);


        builder.setTitle("Change Profile Picture");
        builder.setMessage("Select an image and save!");
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
                ChangeProfilePicFragment.this.getDialog().cancel();
            }
        });

        // Create the dialog
        dialog = builder.create();

        // Set a new onShowListener to override positive button's listener
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // Get the positive button of the dialog
                Button submit = ChangeProfilePicFragment.this.dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                // We override the original listener method because we do not want to exit the dialog
                // until we verify that the entered email is valid
                submit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Call positive button listener's on click method
                        // and pass in current dialog fragment
                        pListener.onDialogPositiveClick(ChangeProfilePicFragment.this);

                        // Don't dismiss
                        // Listener will dismiss when data is validated
                    }
                });
            }
        });



        return dialog;

    } // end onCreateDialog

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View ChangeProfilePicView = inflater.inflate(R.layout.dialog_change_profile_picture, container, false);

        uploadPhotoInput = (TextView) ChangeProfilePicView.findViewById(R.id.uploadTextView);

        // Change color of TextView to hint color
        //uploadPhotoInput.setTextColor();

        // Months are indexed starting at 0, add 1 to month value


        uploadPhotoInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        return ChangeProfilePicView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);

            File imageFile = new File(imagePath);
            String imageName = imageFile.getName();

            uploadPhotoInput.setText(imageName);
            uploadPhotoInput.setTextColor(ContextCompat.getColor(getContext(), R.color.trevorBlue));
        }
    }

    public String getPhoto() {
        Log.d("Editing Prof Pic", imagePath);
        Bitmap bm_original = BitmapFactory.decodeFile(imagePath);

        bm_original = BitmapManip.shrinkToIcon(bm_original);
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Bitmap bm_corrected;
            switch (orientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    bm_corrected = BitmapManip.flipHorizontal(bm_original);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    bm_corrected = BitmapManip.flipVertical(bm_original);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bm_corrected = BitmapManip.rotateClockwise(bm_original, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bm_corrected = BitmapManip.rotateClockwise(bm_original, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bm_corrected = BitmapManip.rotateClockwise(bm_original, 270);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    bm_corrected = BitmapManip.transpose(bm_original);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    bm_corrected = BitmapManip.transverse(bm_original);
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    bm_corrected = bm_original;
            }
            return PictureCompactor.BitmapToStringB64(bm_corrected);
        } catch (IOException e) {
            e.printStackTrace();
            return PictureCompactor.BitmapToStringB64(bm_original);
        }
    }


}
