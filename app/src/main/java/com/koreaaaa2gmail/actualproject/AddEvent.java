package com.koreaaaa2gmail.actualproject;

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
        setContentView(R.layout.activity_add_event);

        quoteView = findViewById(R.id.AddQuoteEvent);
        photoView = findViewById(R.id.AddPhotoEvent);
        textView = findViewById(R.id.AddTextEvent);

        //Hide the Photo and Text Views
        photoView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch(view.getId()) {
            case R.id.radioQuote:
                if(checked) {
                    quoteView.setVisibility(View.VISIBLE);
                    photoView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
                break;
            case R.id.radioText:
                if(checked) {
                    textView.setVisibility(View.VISIBLE);
                    photoView.setVisibility(View.GONE);
                    quoteView.setVisibility(View.GONE);
                }
                break;
            case R.id.radioPhoto:
                if(checked) {
                    photoView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    quoteView.setVisibility(View.GONE);
                }
                break;
        }
    }
}
