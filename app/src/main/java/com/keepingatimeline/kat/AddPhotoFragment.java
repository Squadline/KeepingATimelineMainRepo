package com.keepingatimeline.kat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Trevor on 5/19/2016.
 */
public class AddPhotoFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView uploadedPhoto;
    Button bImportPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View AddPhotoFragmentView = inflater.inflate(R.layout.add_photo_fragment, container, false);

        bImportPhoto = (Button) AddPhotoFragmentView.findViewById(R.id.importPhoto);
        uploadedPhoto = (ImageView) AddPhotoFragmentView.findViewById(R.id.uploadedImage);

        bImportPhoto.setOnClickListener(new View.OnClickListener() {
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
            uploadedPhoto.setImageURI(selectedImage);
        }
    }
}