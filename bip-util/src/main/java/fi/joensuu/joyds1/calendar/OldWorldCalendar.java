/* fi/joensuu/joyds1/calendar/OldWorldCalendar.java
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
 * This class has functions and constants for
 * calendars used in Europe, Asia and Africa.
 */
abstract class OldWorldCalendar extends YMD
{
  /**
   * Get day of week of this date.<p>
   *
   * First day of week is Monday according to ISO Standard ISO-8601.<p>
   *
   * 1=MONDAY, 2=TUESDAY, 3=WEDNESDAY,
   * 4=THURSDAY, 5=FRIDAY, 6=SATURDAY, 7=SUNDAY.
   */
  public int getDayOfWeek()
  {
    int n = (getJulianDayNumber() + 1) % 7;
    return ((n == 0) ? 7 : n);
  }


  /**
   * Get day of year of this date. First day of year is 1.
   */
  public final int getDayOfYear()
  {
    return date2doy();
  }


  /**
   * Is this year (i.e. getYear()) a leap year?
   */
  public final boolean isLeapYear()
  {
    return isLeapYear (getYear());
  }


  /**
   * Is (year, month, day) a legal date in this calendar?
   *
   * @param year  Year.
   * @param month Month.
   * @param day   Day.
   */
  public boolean isDate (int year, int month, int day)
  {
    // Override this method if years do not start at 1.
    //
    return ((year > 0) &&
            (1 <= month && month <= getLastMonthOfYear (year)) &&
            (1 <= day && day <= getLengthOfMonth (year, month)));
  }


  /**
   * Is (year, doy) a legal date in this calendar?
   *
   * @param year Year.
   * @param doy  Day of year. First day of year is 1.
   */
  public boolean isDate (int year, int doy)
  {
    // Override this method if years do not start at 1.
    //
    return ((year > 0) && (1 <= doy && doy <= getLengthOfYear (year)));
  }


  /**
   * Set this date to (year, month, day).
   */
  public void set (int year, int month, int day)
  {
    this.year = year;
    this.month = month;
    this.day = day;
    this.jdn = date2jdn (getYear(), getMonth(), getDay());
    ok ("OldWorldCalendar set (int year, int month, int day)");
  }


  /**
   * Set this date to (year, doy).
   *
   * @param year Year.
   * @param doy  Day of year. First day of year is 1.
   */
  public void set (int year, int doy)
  {
    this.year = year;
    doy2date (year, doy);
    this.jdn = date2jdn (getYear(), getMonth(), getDay());
    ok ("OldWorldCalendar set (int year=" + year + ", int doy=" + doy + ")");
  }


  /**
   * Test if this is a legal date and throw IllegalArgumentException
   * if it is not.
   */
  protected boolean ok (String message)
  {
    if (isDate (getYear(), getMonth(), getDay())) {
      return true;
    }
    else {
      throw new IllegalArgumentException
        (message + ":\n" +
         " year=" + getYear() +
         " month=" + getMonth() +
         " day=" + getDay() +
         " jdn=" + getJulianDayNumber());
    }
  }


  /**
   * Convert this date to Julian day number.
   */
  protected int date2jdn()
  {
    return date2jdn (getYear(), getMonth(), getDay());
  }


  /**
   * Convert date to day of year.
   */
  protected int date2doy()
  {
    return (getCumulativeDays (getYear(), getMonth()-1) + getDay());
  }


  public int getLengthOfYear (int year)
  {
    return getCumulativeDays (year, getLastMonthOfYear (year));
  }


  /**
   * Return the first month of year.<p>
   *
   * This function always returns 1.
   */
  public int getFirstMonthOfYear (int year)
  {
    return 1;
  }


  /**
   * Return the last month of year.<p>
   *
   * This function always returns 12.
   */
  public int getLastMonthOfYear (int year)
  {
    return 12;
  }


  /**
   * Return the first day of month.<p>
   *
   * This function always returns 1.
   */
  public int getFirstDayOfMonth (int year, int month)
  {
    return 1;
  }


  /**
   * Return the last day of month.<p>
   */
  public int getLastDayOfMonth (int year, int month)
  {
    return getLengthOfMonth (year, month);
  }


  /**
   * Number of days in 400 years in Gregorian calendar.
   */
  protected static final int D400 = 146097;


  /**
   * Number of days in 100 years in Gregorian calendar.
   */
  protected static final int D100 = 36524;


  /**
   * Number of days in 4 years in Gregorian calendar.
   */
  protected static final int D4 = 1461;


  private final void jdn (int b, int c)
  {
    int d = (4*c + 3) / 1461;
    int e = c - 1461 * d / 4;
    int m = (5*e  + 2) / 153;

    year  = 100*b + d - 4800 + (m / 10);
    month = m + 3 - 12 * (m / 10);
    day   = e - (153*m + 2) / 5 + 1;
  }


  /**
   * Convert Julian day number to Gregorian date.
   */
  protected final void jdn2date_gregorian (int n)
  {
    int a = n + 32044;
    int b = (4*a + 3) / 146097;
    int c = a - (146097 * b) / 4;

    jdn (b, c);
  }


  /**
   * Convert Julian day number to Julian date.
   */
  protected final void jdn2date_julian  (int n)
  {
    jdn (0, n + 32082);
  }


  /**
   * Convert number to (year, month, day).<p>
   *
   * This function works for any calendar whose leap year structure
   * and the length of years (365 or 366) are the same as those
   * of Gregorian calendar. That is, the number of months and/or the
   * length of months can be different than in Gregorian calendar.<p>
   *
   * This is an auxiliary function used by some subclasses of private
   * class OldWorldCalendar to convert Julian day number to date.
   *
   * @param year0 Number of years to add to the calculated year.
   * @param d0    Number to be converted.
   */
  protected void jdn2ymd (int year0, int d0)
  {
    // Algorithm is modified from
    // Edward M. Reingold, Nachum Dershowitz and Stewart M. Clamen:
    // Calendrical Calculations, II: Three Historical Calendars.
    // Software-- Practice & Experience,
    // vol. 23(4), 383--404 (April 1993); page 384.
    // http://emr.cs.iit.edu/~reingold/calendar2.ps
    //
    // In the calculations of n100 and n1, Math.min takes care of leap years.

    final int n400 = d0 / D400;                   // Number of completed 400-year cycles.
    final int d400 = amod (d0+1, D400);           // Days not in n400.
    final int n100 = Math.min (3, (d400-1)/D100); // Number of 100-year cycles.
    final int d100 = d400 - D100*n100;            // Days not in n400 or n100.
    final int n4   = (d100 - 1) / D4;             // Number of 4-year cycles.
    final int d4   = amod (d100, D4);             // Days not in n400, n100 or n4.
    final int n1   = Math.min (3, (d4-1)/365);    // Number of years not in n400, n100 or n4.
    final int d1   = d4 - 365*n1;                 // Days in last  year.

    doy2date (year0 + 400*n400 + 100*n100 + 4*n4 + n1 + 1, d1);
  }
}
