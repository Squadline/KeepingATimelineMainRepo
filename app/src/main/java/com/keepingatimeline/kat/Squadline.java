package com.keepingatimeline.kat;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.Config;

/**
 * Created by Darren on 5/29/2016.
 */
public class Squadline extends Application {

    private final String API_KEY = "DEV574AD3E150F8C8BDDAAB694340E";
    private final String GCM_ID = "731232605931";

    @Override
    public void onCreate() {
        super.onCreate();

        Batch.Push.setGCMSenderId(GCM_ID);
        Batch.setConfig(new Config(API_KEY));
    }
}
