/* fi/joensuu/joyds1/calendar/MayanCalendar.java
   Copyright (C) 2003-2004 Hannu V_is_nen
 
This library is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
  
This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.
 
You should have received a copy of the GNU General Public License
along with this library; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination. */

package fi.joensuu.joyds1.calendar;

/**
 * Mayan calendar.<p>
 *
 * Source is 
 * Edward M. Reingold, Nachum Dershowitz and Stewart M. Clamen:
 * Calendrical Calculations, II: Three Historical Calendars.
 * Software-- Practice & Experience,
 * vol. 23(4), 383--404 (April 1993).<p>
 *
 * You can
 * <a href="http://emr.cs.iit.edu/~reingold/calendar2.ps">download</a>
 * the article.<p>
 *
 * Some code is from emacs's cal-mayan.el by Stewart M. Clamen
 * and Edward M. Reingold.<p>
 *
 * See also
 * <a href="http://www.michielb.nl/maya/astro.html">The Mayan
 * Astronomy Page</a>.<p>
 *
 * This class has four different ways to define the starting point
 * (epoch) of the Mayan calendar.
 */ 
public class MayanCalendar extends Calendar {
  /**
   * Construct a date from current time, using
   * the default time zone and locale.
   */
   public MayanCalendar()
   {
     this (getToday());
   }


  /**
   * Construct a date from (baktun, katun, tun, uinal, kin) using
   * GOODMAN as the epoch of the Mayan long count.
   *
   * @param baktun
   * @param katun
   * @param tun
   * @param uinal
   * @param kin
   */
  public MayanCalendar (int baktun,
                        int katun,
                        int tun,
                        int uinal,
                        int kin)
  {
    this (baktun, katun, tun, uinal, kin, GOODMAN);
  }


  /**
   * Construct a date from Julian day number using
   * GOODMAN as the epoch of the Mayan long count.
   *
   * @param jd Julian day number.
   */
  public MayanCalendar (int jd)
  {
    this (jd, GOODMAN);
  }


  /**
   * Construct a date from (baktun, katun, tun, uinal, kin).
   *
   * @param baktun
   * @param katun
   * @param tun
   * @param uinal
   * @param kin
   * @param epoch Use epoch as the epoch of the Mayan long count.
   *              Value of epoch must be one of GOODMAN_OLD, GOODMAN,
   *              SPINDEN or HOCHLEITNER.
   */
  public MayanCalendar (int baktun,
                        int katun,
                        int tun,
                        int uinal,
                        int kin,
                        int epoch)
  {
    J_FIRST = epoch;
    checkEpoch (epoch);
    set (baktun, katun, tun, uinal, kin);
  }


  /**
   * Construct a date from Julian day number.
   *
   * @param jd Julian day number.
   * @param epoch Use epoch as the epoch of the Mayan long count.
   *              Value of epoch must be one of GOODMAN_OLD, GOODMAN,
   *              SPINDEN or HOCHLEITNER.
   */
  public MayanCalendar (int jd, int epoch)
  {
    J_FIRST = epoch;
    checkEpoch (epoch);
    set (jd);
  }


  /**
   * Construct a date from Calendar using
   * GOODMAN as the epoch of the Mayan long count.
   *
   * @param c Date to be assigned.
   */
  public MayanCalendar (Calendar c)
  {
    this (c, GOODMAN);
  }


  /**
   * Construct a date from java.util.GregorianCalendar using
   * GOODMAN as the epoch of the Mayan long count.
   *
   * @param c Date to be assigned.
   */
  public MayanCalendar (java.util.GregorianCalendar c)
  {
    this (c, GOODMAN);
  }


  /**
   * Construct a date from Calendar.
   *
   * @param c     Date to be assigned.
   * @param epoch Use epoch as the epoch of the Mayan long count.
   *              Value of epoch must be one of GOODMAN_OLD, GOODMAN,
   *              SPINDEN or HOCHLEITNER.
   */
  public MayanCalendar (Calendar c, int epoch)
  {
    J_FIRST = epoch;
    checkEpoch (epoch);
    set (c);
  }


  /**
   * Construct a date from java.util.GregorianCalendar.
   *
   * @param c     Date to be assigned.
   * @param epoch Use epoch as the epoch of the Mayan long count.
   *              Value of epoch must be one of GOODMAN_OLD, GOODMAN,
   *              SPINDEN or HOCHLEITNER.
   */
  public MayanCalendar (java.util.GregorianCalendar c, int epoch)
  {
    J_FIRST = epoch;
    checkEpoch (epoch);
    set (c);
  }


  public int getBaktun() {return baktun;}
  public int getKatun()  {return katun;}
  public int getTun()    {return tun;}
  public int getUinal()  {return uinal;}
  public int getKin()    {return kin;}


  /**
   * Is (baktun, katun, tun, uinal, kin) a legal date in this calendar?
   *
   * @param baktun
   * @param katun
   * @param tun
   * @param uinal
   * @param kin
   */
  public boolean isDate (int baktun,
                         int katun,
                         int tun,
                         int uinal,
                         int kin)
  {
    return ((baktun >= 0) &&
            (0 <= katun  && katun <= 19) &&
            (0 <= tun    && tun   <= 19) &&
            (0 <= uinal  && uinal <= 17) &&
            (0 <= kin    && kin   <= 19));
  }


  /**
   * Return textual representation of the date
   * in this format: baktun.katun.tun.uinal.kin<p>
   *
   * i.e. Values are separated by periods.
   */
  public String toString()
  {
    return (baktun + "." + katun + "." + tun + "." + uinal + "." + kin);
  }


  /**
   * Set this date to (baktun, katun, tun, uinal, kin)
   *
   * @param baktun
   * @param katun
   * @param tun
   * @param uinal
   * @param kin
   */
  public void set (int baktun,
                   int katun,
                   int tun,
                   int uinal,
                   int kin)
  {
    this.baktun = baktun;
    this.katun = katun;
    this.tun = tun;
    this.uinal = uinal;
    this.kin = kin;

    jdn = date2jdn();

    set();
    ok ("MayanCalendar (int baktun, ...");
  }


  /**
   * Set this date to jd.
   *
   * @param jd Julian day number.
   */
  public void set (int jd)
  {
    jdn2date (jd);
    jdn = jd;
    set();

    ok ("Calendar set (int jd)");
  }


  public int getHaabDay()      {return haabDay;}
  public int getHaabMonth()    {return haabMonth;}
  public int getTzolkinDay()   {return tzolkinDay;}
  public int getTzolkinMonth() {return tzolkinMonth;}


  /**
   * Set Haab and Tzolkin days and months.
   */
  private void set()
  {
    // We must add H0, TD and TM in order to get the Mayan calendar to start
    // on 8 Cumku 4 Ahau, because our first day (Julian day number 0 or
    // 1 January 4714 BC in proleptic Julian calendar) is different from
    // Reingold et.al. (3 January 1 AD in proleptic Julian calendar or
    // 1 January 1 in proleptic Gregorian calendar). In addidion, we start
    // day numbering from zero, Reingold et.al. start from 1.
    //
    // If 'R' is day number in Reingold et.al., its Julian day number 'J' is
    //
    // J = 1721426 + R - 1
    //
    // where 1721426 is Julian day number for 3 January 1 AD
    // in proleptic Julian calendar.

    final int day_of_haab = (getJulianDayNumber() + H0 +
                             + FIRST_HAAB_DAY
                             + 20 * (FIRST_HAAB_MONTH - 1)) % 365;

    haabDay   = day_of_haab % 20;
    haabMonth = day_of_haab / 20 + 1;

    tzolkinDay   = amod ((getJulianDayNumber() + FIRST_TZOLKIN_DAY   + TD), 13);
    tzolkinMonth = amod ((getJulianDayNumber() + FIRST_TZOLKIN_MONTH + TM), 20);

////System.out.println ("haab " + day_of_haab + " " +
//                    haabDay + " " + haabMonth + " " +
//                    tzolkinDay + " " + tzolkinMonth);
  }


  /**
   * Test if this is a legal date and throw IllegalArgumentException
   * if it is not.
   */
  protected boolean ok (String message)
  {
    if (isDate (baktun, katun, tun, uinal, kin) && jdn >= J_FIRST) {
      return true;
    }
    else {
      throw new IllegalArgumentException
        (message + ":\n" +
         " baktun=" + baktun +
         " katun="  + katun +
         " tun="    + tun +
         " uinal="  + uinal +
         " kin="    + kin);
    }
  }


  /**
   * Convert date to Julian day number.
   */
  protected int date2jdn()
  {
    return (BAKTUN * baktun +
            KATUN  * katun  +
            TUN    * tun    +
            UINAL  * uinal  +
                     kin    + J_FIRST);
  }


  /**
   * Convert Julian day number to date.
   *
   * @param n Julian day number.
   */
  protected void jdn2date (int n)
  {
    if (n < J_FIRST) {
      throw new IllegalArgumentException ("MayanCalendar jdn2date n=" + n);
    }

    baktun = (n - J_FIRST) / BAKTUN;

    final int day_of_baktun = (n - J_FIRST) % BAKTUN;

    katun = day_of_baktun / KATUN;

    final int day_of_katun = day_of_baktun % KATUN;

    tun = day_of_katun / TUN;

    final int day_of_tun = day_of_katun % TUN;

    uinal = day_of_tun / UINAL;
    kin = day_of_tun % UINAL;
  }


  private final void checkEpoch (int epoch)
  {
     // See comment at the start of method set().
     // The values for H0, TD and TM was found by trial and error.

     switch (epoch) {
       case GOODMAN_OLD:
         H0 = 80;
         TD =  0;
         TM = 15;
         break;
       case GOODMAN:
         H0 = 82;
         TD =  2;
         TM = 17;
         break;
       case SPINDEN:
         H0 = 81;
         TD =  1;
         TM = 16;
         break;
       case HOCHLEITNER:
         H0 = 305;
         TD = 19;
         TM = 15;
         break;
       default:
         throw new IllegalArgumentException ("checkEpoch epoch=" + epoch);
     }
  }

  private int baktun;
  private int katun;
  private int tun;
  private int uinal;
  private int kin;

  private int haabDay;
  private int haabMonth;
  private int tzolkinDay;
  private int tzolkinMonth;

  private final int J_FIRST;


  /**
   * Goodman-Martinez-Thompson's value for the epoch of the Mayan long
   * count from
   * <a href="http://emr.cs.iit.edu/~reingold/calendar2.ps">Reingold et.al.</a>
   * 8 September 3114 B.C.
   */
  public static final int GOODMAN_OLD = 584285;


  /**
   * Goodman-Martinez-Thompson's value for the epoch of the Mayan long
   * count from emacs's cal-mayan.el. 6 September 3114 B.C.
   *
   * Reingold's
   * <a href="http://emr.cs.iit.edu/home/reingold/calendar-book/Calendrica.html">Calendrica</a>
   * seems to use this value.
   */
  public static final int GOODMAN = 584283;


  /**
   * Spinden's value for the epoch of the Mayan long count from
   * <a href="http://emr.cs.iit.edu/~reingold/calendar2.ps">Reingold et.al.</a>
   * 11 November 3374 B.C.
   */
  public static final int SPINDEN = 489384;


  /**
   * Hochleitner's value for the epoch of the Mayan long count from
   * emacs's cal-mayan.el. 30 January 3128 B.C.
   */
  public static final int HOCHLEITNER = 578585;


  private static final int BAKTUN = 144000;
  private static final int KATUN  =   7200;
  private static final int TUN    =    360;
  private static final int UINAL  =     20;

  private static final int FIRST_HAAB_DAY = 8;
  private static final int FIRST_HAAB_MONTH = 18;

  private static final int FIRST_TZOLKIN_DAY = 4;
  private static final int FIRST_TZOLKIN_MONTH = 20;

  private int H0;
  private int TD;
  private int TM;


  /** Julian day number for the start of Mayan calendar:
   * Goodman-Martinez-Thompson's value: 6 September 3114 B.C.
   */
  public static final int FIRST_JULIAN_DAY = GOODMAN;


  /** Julian day number for the start of Mayan calendar:
   * Goodman-Martinez-Thompson's value: 6 September 3114 B.C.
   */
  public int getFirstJulianDay()
  {
    return FIRST_JULIAN_DAY;
  }
}
