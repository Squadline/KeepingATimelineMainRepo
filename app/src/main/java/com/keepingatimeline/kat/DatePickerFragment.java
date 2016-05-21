package com.keepingatimeline.kat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Trevor on 5/20/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Use the current date as the default date in the date picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        TextView dateQuoteInput = (TextView) getActivity().findViewById(R.id.dateQuoteInput);

        //Months are indexed starting at 0, add 1 to month value
        String dateInput = (month + 1) + "/" + day + "/" + year;
        dateQuoteInput.setText(dateInput);
    }
}
