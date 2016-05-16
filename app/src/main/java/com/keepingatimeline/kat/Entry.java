/**
 * Entry.java for CSE 110 KATS!!
 *
 * String max 10 mb
 * EntryQuote - s quote, s speaker, 3xi date, 
 * EntryText - s text, 3xi date
 * EntryPic - s pic,s desci, s location, 3xi date   
 * HOW STORE PIC IN FIREBASE
 */

public class Entry
{
  int d;
  int m;
  int y;

  // s1 q: t, t: t, p: p
  // loc = loc
  String loc, s1, s2;

  // q s2 speaker, picture s2 descrip
 
  Entry(int d, int m, int y)
  {
    this.d = d;
    this.m = m;
    this.y = y;
  }

}
