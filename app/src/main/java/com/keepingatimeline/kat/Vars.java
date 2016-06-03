package com.keepingatimeline.kat;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * Created by Dana on 5/22/2016.
 */
public final class Vars {

    private static final String FIREBASE_ADDRESS = "https://fiery-fire-8218.firebaseio.com/";
    private static final String EMPTY = "";
    private static final String TIMELINE_ADDRESS = "Timelines/";
    private static final String USER_ADDRESS = "Users/";

    private static Firebase FB_REF = null;
    private static String currUserEmail;

    private Vars(){}

    public static Firebase getFirebase() {
        if(FB_REF == null) {
            FB_REF = new Firebase(FIREBASE_ADDRESS);
        }

        return FB_REF;
    }

    public static String getUID() {
        AuthData auth = getFirebase().getAuth();
        if(auth == null) return EMPTY;
        else return auth.getUid();
    }

    public static Firebase getTimeline(String timelineID) {
        if(timelineID == null) return getFirebase();
        else return getFirebase().child(TIMELINE_ADDRESS + timelineID);
    }

    public static  Firebase getUser(String userID) {
        if(userID == null) return getFirebase();
        else return getFirebase().child(USER_ADDRESS + userID);
    }

    public static String getUserEmail(){
        return currUserEmail;
    }

    public static Firebase getCurrentUser() {
        return getFirebase().child(USER_ADDRESS + getUID());
    }
}
