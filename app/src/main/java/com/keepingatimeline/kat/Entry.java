/**
 * Entry.java for CSE 110 KATS!!
 *
 * String max 10 mb
 * EntryQuote - s quote, s speaker, 3xi date, 
 * EntryText - s text, 3xi date
 * EntryPic - s pic,s desci, s location, 3xi date   
 * HOW STORE PIC IN FIREBASE
 */

/*

public class Entry
{
  int entryId;
  int d;
  int m;
  int y;
  int mode; // 0 = q, 1 = t, 2 = p
  // s1 q: t, t: t, p: p
  // s2 q: s, t: ?, p: ? do pictures have descriptions?
  // loc = loc
  String loc, s1, s2;

  // q s2 speaker, picture s2 descrip
 
  Entry(int id)
  {
    // save id
    entryId = id;
    
    // grab entry data - date, location
    // how is date stored in db?
    // we need DATE CREATEd & DATE entered
    //User.fbRef.child(User.EVENT + entryId + "/" + User.DATE)
    //?  

    User.fbRef.child(User.EVENT + entryId + "/" + User.LOC).addListenerForSingleValueEvent(
          new ValueEventListener() 
          {
            @Override
            public void onDataChange(DataSnapshot ss)
            {
              loc = ss.getValue();
            }

            @Override
            public void onCancelled(FirebaseError err)
            {
              Log.e("Read Entry Location Failed: " + err.getMessage() );
            }
          });
    
    // get mode?

    this.d = d;
    this.m = m;
    this.y = y;
    
  }



}
*/
