package com.keepingatimeline.kat;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * Created by Dana on 5/22/2016.
 */
public final class Vars {

    private static Firebase FB_REF = null;

    private Vars(){}

    public static Firebase getFirebase() {
        if(FB_REF == null) {
            FB_REF = new Firebase("https://fiery-fire-8218.firebaseio.com/");
        }

        return FB_REF;
    }

    public static String getUID() {
        AuthData auth = getFirebase().getAuth();
        if(auth == null) return "";
        else return auth.getUid();
    }

    public static Firebase getTimeline(String timelineID) {
        if(timelineID == null) return getFirebase();
        else return getFirebase().child("Timelines/" + timelineID);
    }

}
