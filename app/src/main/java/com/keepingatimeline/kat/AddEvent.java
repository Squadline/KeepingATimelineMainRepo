package com.keepingatimeline.kat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class AddEvent extends AppCompatActivity {
    private View quoteView;
    private View photoView;
    private View textView;
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.keepingatimeline.kat.R.layout.activity_add_event);

        quoteView = findViewById(com.keepingatimeline.kat.R.id.AddQuoteEvent);
        photoView = findViewById(com.keepingatimeline.kat.R.id.AddPhotoEvent);
        textView = findViewById(com.keepingatimeline.kat.R.id.AddTextEvent);

        //Hide the Photo and Text Views
        photoView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
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
}
