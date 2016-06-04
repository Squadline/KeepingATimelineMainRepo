package com.keepingatimeline.kat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Darren on 6/3/2016.
 */
public class PushReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the PushService component and start wakeful service
        ComponentName comp = new ComponentName(context.getPackageName(), PushService.class.getName());
        startWakefulService(context, intent.setComponent(comp));
        // Return with code OK
        setResultCode(Activity.RESULT_OK);
    }
}
