package com.keepingatimeline.kat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.batch.android.Batch;
import com.batch.android.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Darren on 5/29/2016.
 */
public class Squadline extends Application {

    private final String API_KEY = "DEV574AD3E150F8C8BDDAAB694340E";
    private final String GCM_ID = "731232605931";

    // List of timelines that the user has created events for recently
    private static ArrayList<String> createdEvents = new ArrayList<String>();

    // Whether or not app is in the foreground
    private static boolean active;

    // Number of messages since user has opened app
    protected static int numMessages;
    // Number of messages for each timeline
    protected static Map<String, Integer> messageCounts = new HashMap<String, Integer>();

    @Override
    public void onCreate() {
        // Call super method and register activity callbacks
        super.onCreate();
        registerActivityLifecycleCallbacks(new MyActivityLifeCycleCallbacks());

        // We'll handle displaying notifications on our own
        Batch.Push.setManualDisplay(true);

        // Set our notification keys
        Batch.Push.setGCMSenderId(GCM_ID);
        Batch.setConfig(new Config(API_KEY));
    }

    // Getter method
    public static boolean isActive() {
        return active;
    }

    // Add to events list
    public static void addCreatedEvent(String event) {
        createdEvents.add(event);
    }

    // Remove from events list
    public static boolean removeCreatedEvent(String event) {
        return createdEvents.remove(event);
    }

    private static final class MyActivityLifeCycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            // We are active again
            // Dismiss all notifications and clear all unread messages
            active = true;
            Batch.Push.dismissNotifications();
            numMessages = 0;
            messageCounts.clear();
            Log.d("Notifications", "Dismissing");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            // App is out of foreground
            active = false;
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}