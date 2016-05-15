package com.keepingatimeline.kat;

import android.app.Activity;
import android.app.Application;

/**
 * Created by boredguy88 on 5/12/2016.
 */
public class Temp extends Application {

    private String temp;

    public Temp(String input){
        this.temp = input;
    }
    public String get()
    {
        return this.temp;
    }

    public void set(String x){
        this.temp = x;
    }
}
