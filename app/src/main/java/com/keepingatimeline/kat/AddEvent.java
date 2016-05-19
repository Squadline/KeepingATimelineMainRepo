package com.keepingatimeline.kat;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.firebase.client.Firebase;

public class AddEvent extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1;
    View quoteView;
    View photoView;
    View textView;
    ImageView uploadedPhoto;
    Button bImportPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.keepingatimeline.kat.R.layout.activity_add_event);
        Firebase.setAndroidContext(this);
        
        quoteView = findViewById(com.keepingatimeline.kat.R.id.AddQuoteEvent);
        photoView = findViewById(com.keepingatimeline.kat.R.id.AddPhotoEvent);
        textView = findViewById(com.keepingatimeline.kat.R.id.AddTextEvent);
        bImportPhoto = (Button)findViewById(R.id.importPhoto);
        uploadedPhoto = (ImageView)findViewById(R.id.uploadedImage);

        //Hide the Photo and Text Views
        photoView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        //Create onClickListeners for Buttons
        bImportPhoto.setOnClickListener(this);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch(view.getId()) {
            case com.keepingatimeline.kat.R.id.radioQuote:
                if(checked) {
                    quoteView.setVisibility(View.VISIBLE);
                    photoView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
                break;
            case com.keepingatimeline.kat.R.id.radioText:
                if(checked) {
                    textView.setVisibility(View.VISIBLE);
                    photoView.setVisibility(View.GONE);
                    quoteView.setVisibility(View.GONE);
                }
                break;
            case com.keepingatimeline.kat.R.id.radioPhoto:
                if(checked) {
                    photoView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    quoteView.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.importPhoto:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            uploadedPhoto.setImageURI(selectedImage);
        }
    }
}
