package com.keepingatimeline.kat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class AddEvent extends AppCompatActivity {

    /*
    private static final int RESULT_LOAD_IMAGE = 1;
    View quoteView;
    View photoView;
    View textView;
    ImageView uploadedPhoto;
    Button bImportPhoto; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.keepingatimeline.kat.R.layout.activity_add_event);
        Firebase.setAndroidContext(this);

        // Uses a Toolbar as an ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView postText = (TextView) findViewById(R.id.postText);
        TextView cancelText = (TextView) findViewById(R.id.cancelText);

        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
        // Adds back button to toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable drawableClose = ContextCompat.getDrawable(this, R.drawable.ic_close);
        drawableClose = DrawableCompat.wrap(drawableClose);
        DrawableCompat.setTint(drawableClose, ContextCompat.getColor(this, R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(drawableClose); */

        TabLayout tabLayout = (TabLayout) findViewById(R.id.eventTabs);
        tabLayout.addTab(tabLayout.newTab().setText("Photo"));
        tabLayout.addTab(tabLayout.newTab().setText("Quote"));
        tabLayout.addTab(tabLayout.newTab().setText("Text"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.eventPager);
        final EventPagerAdapter eventAdapter = new EventPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(eventAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(Color.GRAY, Color.WHITE);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setCurrentItem(1);
    }/*
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
    }*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_event_menu, menu);

        Drawable drawableCheck = menu.findItem(R.id.postEvent).getIcon();
        drawableCheck = DrawableCompat.wrap(drawableCheck);
        DrawableCompat.setTint(drawableCheck, ContextCompat.getColor(this, R.color.white));
        menu.findItem(R.id.postEvent).setIcon(drawableCheck);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
