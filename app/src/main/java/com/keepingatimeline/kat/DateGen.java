package com.keepingatimeline.kat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dana on 5/30/2016.
 */
public final class DateGen {

    private DateGen() {}

    public static String getCurrentDate() {
        SimpleDateFormat todaysDate = new SimpleDateFormat("MM/dd/yyyy");
        return todaysDate.format(new Date());
    }
}
