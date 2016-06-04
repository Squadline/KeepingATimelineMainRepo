package com.keepingatimeline.kat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.batch.android.Batch;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Darren on 6/3/2016.
 */
public class PushService extends IntentService {

    private final String ID_KEY = "key";
    private final String NAME_KEY = "name";

    private final String ID_EXTRA = "Timeline ID";
    private final String NAME_EXTRA = "Timeline Name";

    // Mapping for Timeline IDs to Timeline Names
    private static Map<String, String> idToName = new HashMap<String, String>();

    public PushService() {
        super("MyPushService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Try - finally
        try {
            // If this is for Batch
            if (Batch.Push.shouldDisplayPush(this, intent)) {
                // Get the passed in ID and name of the timeline
                String timelineID = intent.getStringExtra(ID_KEY);
                String timelineName = intent.getStringExtra(NAME_KEY);

                // Add them to the id to name mapping
                idToName.put(timelineID, timelineName);

                // Increment the number of unread messages
                Squadline.numMessages++;

                // If this timeline has previously unread messages
                if (Squadline.messageCounts.containsKey(timelineID)) {
                    // Increment the number of unread messages for the timeline
                    Squadline.messageCounts.put(timelineID, Squadline.messageCounts.get(timelineID) + 1);
                }
                // If it does not have any
                else {
                    // Put it into the map with a count of 1
                    Squadline.messageCounts.put(timelineID, 1);
                }

                // This is our own notification ID
                // We will always use the same ID so that we update our previous notifications
                // rather than constantly pushing new ones
                int id = 1;

                // If the user did not create this event and he is not looking at the app right now
                if (!Squadline.removeCreatedEvent(timelineID) && !Squadline.isActive()) {
                    // Create a notification builder with the information passed in from Batch
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setLargeIcon(BitmapCache.getBitmapFromMemCache(timelineID))
                            .setContentTitle(intent.getStringExtra(Batch.Push.TITLE_KEY))
                            .setContentText(intent.getStringExtra(Batch.Push.ALERT_KEY))
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setDefaults(Notification.DEFAULT_ALL);

                    // If there is no cached icon for this timeline right now, use the default launcher icon
                    if (BitmapCache.getBitmapFromMemCache(timelineID) == null) {
                        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    }

                    // Create an InboxStyle to easily display all unread timelines
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    inboxStyle.setBigContentTitle(Squadline.numMessages + " new events!");

                    // Go over all the unread timelines and add their counts to the notification
                    for (Map.Entry<String, Integer> entry : Squadline.messageCounts.entrySet()) {
                        // Singular version for grammar
                        if (entry.getValue() == 1) {
                            inboxStyle.addLine(entry.getValue() + " new event in " + idToName.get(entry.getKey()) + "!");
                        }
                        else {
                            inboxStyle.addLine(entry.getValue() + " new events in " + idToName.get(entry.getKey()) + "!");
                        }
                    }

                    // Declare the destination intent and create a stack builder for the intent
                    Intent resultIntent;
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    // If only one timeline has been unread
                    if (Squadline.messageCounts.size() == 1) {
                        // Lead the user to that timeline when they click the notification
                        // Pass in the ID and name as extras for correct operation
                        resultIntent = new Intent(this, ViewTimeline.class);
                        resultIntent.putExtra(ID_EXTRA, timelineID);
                        resultIntent.putExtra(NAME_EXTRA, timelineName);
                        // Create the backtrack stack for the timeline activity
                        stackBuilder.addParentStack(ViewTimeline.class);
                        // If there are multiple unread messages in the timeline
                        // display the correct number
                        if (Squadline.numMessages > 1) {
                            mBuilder.setContentText(Squadline.numMessages + " new events in " + timelineName + "!");
                        }
                    }
                    // If there are multiple unread timelines
                    else {
                        // Lead the user to the Main Screen
                        resultIntent = new Intent(this, MainScreen.class);
                        // Create the backtrack stack
                        stackBuilder.addParentStack(MainScreen.class);
                        // Assign the InboxStyle to the notification to show all unread timelines
                        mBuilder.setStyle(inboxStyle);
                        // Set the notification icon back to default
                        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    }

                    // Add the destination intent to the stack
                    stackBuilder.addNextIntent(resultIntent);
                    // Create a pending intent and add to the notification builder
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    // Get the notification manager
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    // Build the notification and notify the manager with our ID
                    mNotificationManager.notify(id, mBuilder.build());

                    // Allows Batch to keep track of notifications
                    Batch.Push.onNotificationDisplayed(this, intent);
                }
                else {
                    // If the user either created event or is looking at the app
                    // don't display the notification and clear all unread counts
                    Squadline.numMessages = 0;
                    Squadline.messageCounts.clear();
                }
            }
        }
        finally {
            // Complete the intent from the PushReceiver
            PushReceiver.completeWakefulIntent(intent);
        }
    }
}
