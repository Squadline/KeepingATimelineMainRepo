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
public class AddQuoteFragment extends Fragment {
    EditText title, quote, source;
    TextView dateQuoteInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View AddQuoteFragmentView = inflater.inflate(R.layout.add_quote_fragment, container, false);

        dateQuoteInput = (TextView) AddQuoteFragmentView.findViewById(R.id.dateQuoteInput);
        title = (EditText) AddQuoteFragmentView.findViewById(R.id.quoteTitle);
        quote = (EditText) AddQuoteFragmentView.findViewById(R.id.quoteData);
        source = (EditText) AddQuoteFragmentView.findViewById(R.id.quoteSource);

        // Months are indexed starting at 0, add 1 to month value
        String currentDate = (month + 1) + "/" + day + "/" + year;
        dateQuoteInput.setText(currentDate);

        dateQuoteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerQuote();
                newFragment.show(getFragmentManager(),"Date Picker");
            }
        });

        return AddQuoteFragmentView;
    }

    public void emptyTexts() {
        title.setText("");
        quote.setText("");
        source.setText("");
    }

    public String getTitle() {
        return title.getText().toString();
    }


    public String getDate() {
        return dateQuoteInput.getText().toString();
    }

    public String getQuote() {
        return quote.getText().toString();
    }

    public String getSource() {
        return source.getText().toString();
    }
}