/* fi/joensuu/joyds1/calendar/JalaliCalendar.java
   Copyright (C) 2004-2005 Hannu V_is_nen
 
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
 * Persian
 * <a href="http://www.astro.uni.torun.pl/~kb/Papers/EMP/PersianC-EMP.htm">Jalali
 * calendar</a>.<p>
 *
 * Jalali calendar has 12 months. First six months have 31 days, next five
 * months have 30 days and the last month have 29 days on ordinary years and
 * 30 days on leap years. The year starts on vernal (spring) equinox (around
 * 20 March) if that occurs before noon (12:00) Tehran time, otherwise the
 * next day.<p>
 *
 * Leap year rules are based on astronomical observations. 
 * Kazimierz M. Borkowski's article
 * <a href="http://www.astro.uni.torun.pl/~kb/Papers/EMP/PersianC-EMP.htm">The
 * Persian calendar for 3000 years</a> says
 *
 * ''As expected, roughly every fourth year in the Persian calendar is
 * the leap one. This is the well known regularity in solar calendars.
 * However, normally after every 32 (sometimes after 28 or 36) years
 * follows an extra common year making four consecutive 365-day years
 * instead of the usual three. Currently the leap years go smoothly in
 * the 33-year cycles and specifically they are those years that after
 * dividing by 33 leave a remainder of 1, 5, 9, 13, 17, 22, 26 and 30.
 * E.g., the Jalaali year 1375 that begun on March 20, 1996 has the
 * remainder of 22 and thus is the leap year.''<p>
 *
 * The 33-year rule is implemented in the 
 * <a href="http://www.payvand.com/calendar/">Khayam</a>
 * Persian calendar program. Borkowski says the rule is valid for Jalali
 * years 1178 to 1634 (Gregorian 1799 to 2256).<p>
 *
 * This class calculates the leap years according to Borkowski's
 * <a href="http://www.astro.uni.torun.pl/~kb/Papers/EMP/PersianC-EMP.htm">article</a>
 * for Jalali calendar years 1 thru 3177 (Gregorian years 622 thru 2799) and
 * after that this class uses the 33-year rule.<p>
 *
 * See also
 * <a href="http://www.wikipedia.org/wiki/Persian_calendar">Wikipedia</a> and
 * <a href="http://www.wikipedia.org/wiki/Norouz">Norouz</a> and the
 * <a href="http://www.tondering.dk/claus/cal/node6.html">Calendar FAQ</a>.<p>
 *
 * Class {@link fi.joensuu.joyds1.calendar.PersianCalendar PersianCalendar}
 * calculates leap years using the 2820-year rule from the
 * <a href="http://www.tondering.dk/claus/cal/node6.html">Calendar FAQ</a>.<p>
 */
public class JalaliCalendar extends OldWorldCalendar {
  /**
   * Construct a date from current time, using
   * the default time zone and locale.
   */
   public JalaliCalendar()
   {
     this (getToday());
   }


  /**
   * Construct a date from year, month and day.
   *
   * @param year  Year.
   * @param month Month.
   * @param day   Day.
   */
  public JalaliCalendar (int year, int month, int day)
  {
    set (year, month, day);
  }


  /**
   * Construct a date from year and day of year.
   *
   * @param year  Year.
   * @param doy   Day of year. First day of year is 1.
   */
  public JalaliCalendar (int year, int doy)
  {
    set (year, doy);
  }


  /**
   * Construct a date from Julian day number.
   *
   * @param jd Julian day number.
   */
  public JalaliCalendar (int jd)
  {
    set (jd);
  }


  /**
   * Construct a date from Calendar.
   *
   * @param c Date to be assigned.
   */
  public JalaliCalendar (Calendar c)
  {
    set (c);
  }


  /**
   * Construct a date from java.util.GregorianCalendar.
   *
   * @param c Date to be assigned.
   */
  public JalaliCalendar (java.util.GregorianCalendar c)
  {
    set (c);
  }


  /**
   * Convert the Julian Day number to a date in the Jalaali calendar
   */
  protected void jdn2date (int n)
  {
    if (n < FIRST_JULIAN_DAY) {
      throw new IllegalArgumentException ("JalaliCalendar jdn2date (int n)");
    }

    boolean done = false;

    jdn2date_gregorian (n);  // Convert Julian day number to Gregorian date.
    year -= 621;             // Calculate Jalali year.
    JalCal (year);
    int j = date2jdn_gregorian (year+621, 3, March); // Start of Jalali year.

    // Find number of days that passed since 1 Farvardin.
    //
    int k = n - j;

////System.out.println ("a " + k + " " + n + " " + j);

    if (k >= 0) {
      if (k <= 185) { // First 6 months.
        month = 1 + k/31;
        day = (k % 31) + 1;
        done = true;
      }
      else { // The remaining months.
        k -= 186;
      }
    }
    else {       // Pervious Jalali year.
      year -= 1;
      k += 179;
      if (leap == 1) {
        k++;
      }
    }

    if (!done) {
      month = 7 + k/30;
      day = k % 30 + 1;
    }

    if (!isDate (year, month, day)) {
      throw new IllegalArgumentException
        ("JalaliCalendar jdn2date " +
         "year=" + year + " month=" + month + " day=" + day + " n=" + n);
    }
  }


  protected int date2jdn (int year, int month, int day)
  {
    JalCal (year);
    return (date2jdn_gregorian (Gy,3,March) + 31*(month-1) - (month/7)*(month-7) + day - 1);
  }


  protected void doy2date (int year, int doy)
  {
    if (doy < 1 || doy > getLengthOfYear (year)) {
      throw new IllegalArgumentException
        ("JalaliCalendar doy2date year=" + year + " doy=" + doy);
    }

    this.year = year;

    if (doy <= cumulative_days_in_ordinary_year[6]) {
      this.month = (doy-1)/31 + 1;
    }
    else {
      this.month = (doy - cumulative_days_in_ordinary_year[6] - 1) / 30 + 7;
    }

    this.day = doy - getCumulativeDays(year,month-1);

    if (!isDate (this.year, this.month, this.day)) {
      throw new IllegalArgumentException
        ("JalaliCalendar doy2date "
         + "this.year=" + this.year + " this.month=" + this.month
         + " this.day=" + this.day + " doy=" + doy);
    }
  }


  protected int getCumulativeDays (int year, int month)
  {
    return (isLeapYear(year)
            ? cumulative_days_in_leap_year[month]
            : cumulative_days_in_ordinary_year[month]);
  }


  public boolean isLeapYear (int year)
  {
    if (year < 3178) {
      JalCal (year);
      return (leap == 0);
    }
    else {
      // Leap year according to the Khayam calendar program.
      //
      switch (year % 33) {
        case  1:
        case  5:
        case  9:
        case 13:
        case 17:
        case 22:
        case 26:
        case 30:
          return true;
        default:
          return false;
      }
    }
  }


  public int getLengthOfMonth (int year, int month)
  {
    return (isLeapYear (year)
            ? days_in_month_in_leap_year[month]
            : days_in_month_in_ordinary_year[month]);
  }


  private static final int cumulative_days_in_ordinary_year[] = {
    0, 31, 62, 93, 124, 155, 186, 216, 246, 276, 306, 336, 365,
  };


  private static final int cumulative_days_in_leap_year[] = {
    0, 31, 62, 93, 124, 155, 186, 216, 246, 276, 306, 336, 366,
  };


  private static final int days_in_month_in_ordinary_year[] = {
    0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29,
  };


  private static final int days_in_month_in_leap_year[] = {
    0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30,
  };


  // Number of days in 33 years.
  //
  private static final int D33 = 12053;  // 33*365 + 8 leap days.


  /** Julian day number for the start of Jalali calendar.
   * Emacs's cal-persia.el says 19 March 622.
   */
  public static final int FIRST_JULIAN_DAY = date2jdn_julian (622, 3, 19);


  public int getFirstJulianDay()
  {
    return FIRST_JULIAN_DAY;
  }


  /**
   * @param Jy Jalali calendar year (-61 to 3177).
   */
  void JalCal (int Jy)
  {
    Gy = Jy + 621;
    int leapJ = -14;
    int jp = breaks[0];
    int jump = 0;

    if (Jy < jp || Jy >= breaks[breaks.length-1]) {
      throw new IllegalArgumentException ("Jy too big");
    }

    // Find the limiting years for the Jalaali year Jy.
    //
    for (int j = 1; j < breaks.length; j++) {
      int jm = breaks[j];
      jump = jm - jp;
      if (Jy < jm)
         break;
      leapJ += (jump/33)*8 + (jump % 33) / 4;
      jp = jm;
    }

    int N = Jy - jp;

    // Find the number of leap years from AD 621 to the beginning
    // of the current Jalaali year in the Persian calendar.
    //
    leapJ += (N/33)*8 + ((N % 33) + 3) / 4;
    if (((jump % 33) == 4) && (jump-N == 4)) {
      leapJ++;
    }

    // And the same in the Gregorian calendar (until the year Gy).
    //
    int leapG = Gy/4 - (Gy/100+1)*3/4 - 150;

    // Determine the Gregorian date of Farvardin the 1st.
    //
    March = 20 + leapJ - leapG;

    // Find how many years have passed since the last leap year.
    //
    if (jump - N < 6) {
      N = N - jump + ((jump + 4)/33)*33;
    }

    leap = (((N+1) % 33)-1) % 4;

    if (leap == -1) {
      leap = 4;
    }
  }

  private int leap;   // Number of years since the last leap year (0 to 4).
  private int Gy;     // Gregorian year of the beginning of Jalali year.
  private int March;  // The March day of Farvardin the 1st (1st day of Jalali year).

  // Jalaali years starting the 33-year rule.
  //
  private static final int[] breaks = {
     -61,    9,   38,  199,  426,  686,  756,  818, 1111, 1181, 
    1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
  };
}
