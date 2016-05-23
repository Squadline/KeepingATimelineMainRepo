package com.keepingatimeline.kat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Trevor on 5/19/2016.
 */
public class AddPhotoFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    TextView datePhotoInput;
    TextView uploadPhotoInput;
    EditText titlePhotoInput;
    EditText photoDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        final ColorStateList hintColors;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View AddPhotoFragmentView = inflater.inflate(R.layout.add_photo_fragment, container, false);

        datePhotoInput = (TextView) AddPhotoFragmentView.findViewById(R.id.datePhotoInput);
        uploadPhotoInput = (TextView) AddPhotoFragmentView.findViewById(R.id.photoName);
        titlePhotoInput = (EditText) AddPhotoFragmentView.findViewById(R.id.photoTitle);
        photoDescription = (EditText) AddPhotoFragmentView.findViewById(R.id.photoDescription);

        hintColors = titlePhotoInput.getHintTextColors();
        uploadPhotoInput.setTextColor(hintColors);

        // Months are indexed starting at 0, add 1 to month value
        String currentDate = (month + 1) + "/" + day + "/" + year;
        datePhotoInput.setText(currentDate);

        datePhotoInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerPhoto();
                newFragment.show(getFragmentManager(),"Date Picker");
            }
        });

        uploadPhotoInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        return AddPhotoFragmentView;
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
            String imagePath = cursor.getString(columnIndex);

            File imageFile = new File(imagePath);
            String imageName = imageFile.getName();

            uploadPhotoInput.setText(imageName);
            uploadPhotoInput.setTextColor(ContextCompat.getColor(getContext(), R.color.trevorBlue));
        }
    }

    public void emptyTexts() {
        titlePhotoInput.setText("");
        photoDescription.setText("");
    }
}