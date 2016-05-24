package com.keepingatimeline.kat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Trevor on 5/19/2016.
 */
public class AddTextFragment extends Fragment {
    EditText text, title;
    TextView dateTextInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View AddTextFragmentView = inflater.inflate(R.layout.add_text_fragment, container, false);

        dateTextInput = (TextView) AddTextFragmentView.findViewById(R.id.dateTextInput);
        title = (EditText) AddTextFragmentView.findViewById(R.id.textTitle);
        text = (EditText) AddTextFragmentView.findViewById(R.id.textBody);

        // Months are indexed starting at 0, add 1 to month value
        String currentDate = (month + 1) + "/" + day + "/" + year;
        dateTextInput.setText(currentDate);

        dateTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerText();
                newFragment.show(getFragmentManager(),"Date Picker");
            }
        });

        return AddTextFragmentView;
    }

    public void emptyTexts() {
        text.setText("");
        title.setText("");
    }

    public String getTitle() {
        return title.getText().toString();
    }


    public String getDate() {
        return dateTextInput.getText().toString();
    }

    public String getText() {
        return text.getText().toString();
    }

}