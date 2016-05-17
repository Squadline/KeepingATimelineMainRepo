/**
 * poop ppoop ppoop
 * Timeline class for CSE 110 KATS
 *
 * 
 */
/*
package com.keepingatimeline.kat;

import android.util.Log;

import java.util.LinkedList;
import java.lang.String;
import java.util.Map;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.view.Event;

public class Timeline
{
  private int tlId;
  private LinkedList<Map.Entry> entryList; // entry, event, whatever
  private String title;
  private String des;
  private boolean admin;
  // better be logged in at this point :0
  Timeline( int id )
  {
    tlId = id;
    // use User.fbRef to grab title and description of tl using tlId
    if( User.fbRef.authData() != null )
    {
      
      User.fbRef.child(User.TIMELINE + tlId + User.NAME ).addListenerForSingleValueEvent( newValueEventListener()
          {
            @Override
            public void onDataChange(DataSnapshot ss)
            {
              title = ss.getValue();
            }

            @Override
            public void onCancelled(FirebaseError err)
            {
              Log.e("Read Timeline Name failed: " + err.getMessage() );
            }
          });

      User.fbRef.child(User.TIMELINE + User.tlId + User.DES ).addListenerForSingleValueEvent( newValueEventListener()
          {
            @Override
            public void onDataChange(DataSnapshot ss)
            {
              des = ss.getValue();
            }

            @Override
            public void onCancelled(FirebaseError err)
            {
              Log.e("Read Timeline Description failed: " + err.getMessage() );
            }
          });
    } else { //hopefully, ref id was not null. 
      Log.e("No Auth Data in TL Constructor :(");
    }
    // title = sfdfd
    // des = fsdfsf
  }

  //smart way to load would be to
  //load first X amt of entries
  //load Entry Meta data to right swipe
  //load first few entries to feed view

  // load more once end hit 
  // OORRR
  //
  // load all entry meta to right swipe and array
  // load data of first few and display in feedview

  public boolean enumEntry()
  { 
    // grab list of entry ids from Timeline table using tlId
 
    User.fbRef.child(User.TIMELINE + tlId + "/" + User.TLENTRY).addListenerForSingleValueEvent( new ValueEventListener()
      {
        @Override
        public void onDataChange(DataSnapshot ss)
        {
          GenericTypeIndicator<List<int>> t = new GenericTypeIndicator<List<int>>() { };
          List<int> eIDs = snapshot.getValue(t);
          if(eIDs == null)
          {
            Log.i("Timeline", "Empty Entry IDs");
          } else {
            Iterator<int> it = eIDs.iterator();
            while( it.hasNext() )
            {
              entryList.add( new Event( it.next() ) );
              //tlList.getLast().getMeta(); // gets name n descrip
            }
          }
        }
        
        @Override
        public void onCancelled(FirebaseError err)
        {
          Log.e("Read entry failed: " + err.getMessage() );   
        }
      });
      


   
    //return true if ok or false if not ok
    if(tlList.size() == 0) // for whatever reason
      return false;
    return true;

    // make Entry objects, load into entryArray. 
  
  }

  //public int getId(); // do we need this?
  public String getTitle()
  {
    return title;
  }
  public String getDescription()
  {
    return des;
  }
  

}
*/