package com.keepingatimeline.kat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Dana on 5/30/2016.
 */
public final class DateGen {

    private DateGen() {}

    public static String getCurrentDate() {
        SimpleDateFormat todaysDate = new SimpleDateFormat("MM/dd/yyyy");
        return todaysDate.format(new Date());
    }

    //Returns if date1 is before date2
    public static boolean compareDates(String date1, String date2) {
        //Holds Data of first date
        int month1 = 0;
        int day1 = 0;
        int year1 = 0;

        //Holds Data of second date
        int month2 = 0;
        int day2 = 0;
        int year2 = 0;

        Scanner s1 = new Scanner(date1).useDelimiter("[^0-9]+");
        Scanner s2 = new Scanner(date2).useDelimiter("[^0-9]+");

        //Store dates into their separate categories
        month1 = s1.nextInt();
        day1 = s1.nextInt();
        year1 = s1.nextInt();

        month2 = s2.nextInt();
        day2 = s2.nextInt();
        year2 = s2.nextInt();

        //Compare Years
        if(year2 != year1) {
            if(year2 > year1)
                return true;
            else
                return false;
        }
        //Compare Months
        else if(month2 != month1) {
            if(month2 > month1)
                return true;
            else
                return false;
        }
        //Compare Days
        else if(day2 != day1) {
            if(day2 > day1)
                return true;
            else
                return false;
        }
        else
            return true;

    }
}
