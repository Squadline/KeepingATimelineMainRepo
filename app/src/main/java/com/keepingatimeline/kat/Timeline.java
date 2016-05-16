/**
 * poop ppoop ppoop
 * Timeline class for CSE 110 KATS
 *
 * 
 */


import java.util.LinkedList;
import java.lang.String;

public class Timeline
{
  private int tlId;
  private LinkedList<Entry> entryArray; // entry, event, whatever
  private String title;
  private String des;
    private boolean admin; 
  // better be logged in at this point :0
  Timeline( int id )
  {
    tlId = id;
    // use User.fbRef to grab title and description of tl using tlId
    User.fbRef
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

    // make Entry objects, load into entryArray. 
  
  }

  public int getId();
  public String getTitle();
  public String getDes();
  public boolean setTitle();
  public boolean setDes();
}
