/* fi/joensuu/joyds1/calendar/PersianCalendar.java
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

package bip.common.util.calendar;

/**
 * <a href="http://www.tondering.dk/claus/cal/node6.html">Persian
 * calendar</a>.<p>
 *
 * See also
 * <a href="http://www.wikipedia.org/wiki/Persian_calendar">here</a> and
 * <a href="http://www.wikipedia.org/wiki/Norouz">here</a>.<p>
 *
 * Some of the code is translated from <code>cal-persia.el</code>
 * in the GNU Emacs distribution.<p>
 *
 * Class {@link fi.joensuu.joyds1.calendar.JalaliCalendar JalaliCalendar}
 * is a better version of the Persian calendar.
 */
public class PersianCalendar extends OldWorldCalendar {
  /**
   * Construct a date from current time, using
   * the default time zone and locale.
   */
   public PersianCalendar()
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
  public PersianCalendar (int year, int month, int day)
  {
    set (year, month, day);
  }


  /**
   * Construct a date from year and day of year.
   *
   * @param year  Year.
   * @param doy   Day of year. First day of year is 1.
   */
  public PersianCalendar (int year, int doy)
  {
    set (year, doy);
  }


  /**
   * Construct a date from Julian day number.
   *
   * @param jd Julian day number.
   */
  public PersianCalendar (int jd)
  {
    set (jd);
  }


  /**
   * Construct a date from Calendar.
   *
   * @param c Date to be assigned.
   */
  public PersianCalendar (Calendar c)
  {
    set (c);
  }


  /**
   * Construct a date from java.util.GregorianCalendar.
   *
   * @param c Date to be assigned.
   */
  public PersianCalendar (java.util.GregorianCalendar c)
  {
    set (c);
  }


  protected void jdn2date (int n)
  {
    if (n < FIRST_JULIAN_DAY) {
      throw new IllegalArgumentException ("PersianCalendar jdn2date (int n)");
    }

    final int d0 = n - FIRST_JULIAN_DAY;

    final int n2820 = d0 / D2820;  // Completed 2820-year cycles.
    final int d1    = d0 % D2820;  // Prior days not in n2820.
    final int y2820 = 2820 * n2820;

    if (d1 >= D2816) {
      //
      // Last 4 years of the last 37-year cycle.
      // We must write ">=" and not ">" because d1's start at 0 not 1.
      //
      final int d2 = d1 - D2816;

      final int y0 = d2 / 365;
      final int d3 = d2 % 365;
      final int yr = y2820 + 2816 + y0;

      if (d2 == 1460) {
        year = yr;
        month = 12;
        day = 30;
      }
      else {
        doy2date (yr + 1, d3 + 1);
      }
    }
    else {
      final int n128 = d1 / D128;   // Completed 128-year cycles.
      final int d4   = d1 % D128;   // Days not in n128.
      final int y128 = 128 * n128;

      if (d4 < D29) {
        //
        // First 29 years in 128-year cycle.
        //
        doy2date_29_33 (y2820 + y128 + 1, d4);
      }
      else {
        //
        // Years 30 thru 128.
        //
        final int d5  = d4 - D29;  // Delete first 29 years.
        final int n33 = d5 / D33;  // Number of completed 33-year cycles.
        final int d6  = d5 % D33;  // Days not in n33.

        doy2date_29_33 (y2820 + y128 + 33*n33 + 30, d6);
      }
    }

    if (!isDate (year, month, day)) {
      throw new IllegalArgumentException
        ("PersianCalendar jdn2date " +
         "year=" + year + " month=" + month + " day=" + day + " n=" + n);
    }
  }


  /**
   * Convert number of days to date in incomplete 29- or 33-year cycle.
   *
   * @param year Number of elapsed years before this cycle.
   * @param n    Number of elapsed days in this cycle.
   *             So n is zero on the first day of the cycle.
   */
  private final void doy2date_29_33 (final int year, final int n)
  {
    this.year = year;

    if (n < 365) {
      //
      // First year. We know that it is not a leap year.
      //
      doy2date (this.year, n + 1);
    }
    else {
      //
      // Years 2 thru 29 or 33. We know that every fourth year
      // (years 5, 9, 13, etc) is a leap year.
      //
      final int d1 = n - 365;    // Delete days in first year.
      final int n4 = d1 / D4;    // Number of 4-year cycles.
      final int d2 = d1 % D4;    // Days not in n4.
      final int n1 = d2 / 365;   // Number of years in d2.
      final int d3 = d2 % 365;   // Number of days not in n4 or n1.
      final int yr = year + 4*n4 + n1;

      if (d2 == 1460 && isLeapYear (yr)) {
        //
        // Last day of leap year.
        //
        this.year = yr;
        this.month = 12;
        this.day = 30;
      }
      else {
        doy2date (yr + 1, d3 + 1);
      }
    }
  }


  protected int date2jdn (int year, int month, int day)
  {
/*
    Simple but slow way to calculate the number of leap days before 'year'.
    int s = 0;
    for (int i = 1; i < year; i++) {
      if (isLeapYear(i))
        s++;
    }
*/

    // Calculate the number of leap days before 'year'.

    final int A = (year - 2) / 2820;   // Number of completed 2820-year cycles.
    final int B = amod (year-1, 2820); // Years not in A.
    final int C = B / 128;   // Number of completed 128-year cycles not in A.
    final int D = B % 128;   // Years not in A and B.

    // Number of completed 33-year cycles.
    //
    final int F = (D <= 29) ? 0 : ((D-30)/33);

    // Number of leap days in last, incomplete 128-year cycle.
    // 29-year cycle has 7 leap days, 33-year cycles have 8 leap days.
    //
    final int G = (D <= 29) ? ((D-1)/4) : (7 + 8*F + cycle(D)/4);


    // 2820-year cycle has 683 leap days, 128-year cycle has 31 leap days.
    //
    final int numberOfLeapDays = 683*A + 31*C + G + ((B == 2820) ? 1 : 0);

    return (FIRST_JULIAN_DAY + 365 * (year-1) + numberOfLeapDays +
            getCumulativeDays (year, month-1) + day - 1);
  }


  protected void doy2date (int year, int doy)
  {
    if (doy < 1 || doy > getLengthOfYear (year)) {
      throw new IllegalArgumentException
        ("PersianCalendar doy2date year=" + year + " doy=" + doy);
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
        ("PersianCalendar doy2date "
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
    // Convert year to integer within 29 or 33 or 37 year cycle
    // and put the result to C. First value within each cycle is 0.
    //
    final int A = amod (year, 2820);
    final int B = amod (A, 128);
    final int C = (A > 2816)
                  ? (A - 2784) // Last 4 years in the last 37-year cycle.
                  : cycle (B);

    return ((C != 0) && (C % 4 == 0));
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


  // Number of days in different number of years.
  //
  private static final int D2820 = 1029983;  // 2820*365 + 683 leap days.
  private static final int D2816 = 1028522;  // 2816*365 + 682 leap days.
  private static final int D128  =   46751;  //  128*365 +  31 leap days.
  private static final int D33   =   12053;  //   33*365 +   8 leap days.
  private static final int D29   =   10592;  //   29*365 +   7 leap days.
  private static final int D4    =    1461;  //    4*365 +   1 leap day.


  /**
   * Convert n to integer within 29 or 33 year cycle.
   * First value within each cycle is 0.
   *
   *  1 => 0 , 2 => 1, ...,  29 => 28,
   * 30 => 0, 31 => 1, ...,  62 => 32,
   * 63 => 0, 64 => 1, ...,  95 => 32,
   * 96 => 0, 97 => 1, ..., 128 => 32.
  */
  private static final int cycle (int n)
  {
    return ((n <= 29) ? (n-1) : ((n-30) % 33));
  }


  /** Julian day number for the start of Persian calendar.
   * Emacs's cal-persia.el says 19 March 622.
   */
  public static final int FIRST_JULIAN_DAY = date2jdn_julian (622, 3, 19);


  public int getFirstJulianDay()
  {
    return FIRST_JULIAN_DAY;
  }
}
