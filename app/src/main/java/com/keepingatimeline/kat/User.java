/**
 *
 * User.java
 * For CSE 110 KATS
 *
 *
 *
 *
 */


/*
//swag
//
package com.keepingatimeline.kat;

import java.util.LinkedList;
import java.util.List;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class User
{
  //firebase reference for all others to use
  public static Firebase fbRef;
  public static final String FBBASE = "https://fiery-fire-8218.firebaseio.com/";
  public static final String USER   = "users/";
  public static final String TLID   = "tl/";
  public static final String NAME   = "name/";
  public static final String DES    = "des/";
  public static final String TIMELINE = "timeline/";

  //private String eMail;
  private LinkedList<Timeline> tlList;
 
  //fire base setup
  
  public User()
  {
    fbSetup();  
    tlList = new LinkedList<Timeline>();
  }
  //public static

  public static void fbSetup()
  {
    fbRef = new Firebase(FBBASE);
  }

  // grab userID or ... ?
  public boolean enumTimeline(int userID )
  {
    //ensure logged in
    if (fbRef.getAuth() == null)
    {
      // not authenticated
      return false;
    }
    
    //grab list of timeline
    Firebase fbTl = fbRef.child(USER + "/" + TL);
    //make new emptyish timelines and add to tlArray
     
    fbTl.addListenerForSingleValueEvent( new ValueEventListener()
      {
        @Override
        public void onDataChange(DataSnapshot ss)
        {
          GenericTypeIndicator<List<int>> t = new GenericTypeIndicator<List<int>>() { };
          List<int> tlIDs = snapshot.getValue(t);
          if(tlIDs == null)
          {
            Log.i("User", "Empty Timeline IDs");
          } else {
            Iterator<int> it = tlIDs.iterator();
            while( it.hasNext() )
            {
              tlList.add( new Timeline( it.next() ) );
              //tlList.getLast().getMeta(); // gets name n descrip
            }
          }
        }
        
        @Override
        public void onCancelled(FirebaseError err)
        {
          Log.e("Read timeline failed: " + err.getMessage() );
        }
      });
      

    //return true if ok or false if not ok
    if(tlList.size() == 0) // for whatever reason
      return false;
    return true;
  }

}
*/