/* fi/joensuu/joyds1/calendar/GregorianCalendarGenerator.java
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
 * Gregorian calendar generator.<p>
 *
 * This class assumes that if the calendar changed in February, the
 * changeover year was not a leap year.<p>
 *
 * This class also assumes that if the calendar changed at the end of
 * the year, last Julian date is 31 December and first Gregorian date
 * is sometimes in January next year.<p>
 *
 * Before the changeover date this class follows the
 * @{link JulianCalendar}.<p>
 */
class GregorianCalendarGenerator extends JulianGregorianCalendar {
  /**
   * Construct a Gregorian calendar. For example of usage, see
   * the source code of {@link GregorianCalendar}.
   * 
   * @param lastJulianYear
   * @param lastJulianMonth
   * @param lastJulianDay
   * @param firstGregorianYear
   * @param firstGregorianMonth
   * @param firstGregorianDay
   */
  protected GregorianCalendarGenerator
              (int lastJulianYear,
               int lastJulianMonth,
               int lastJulianDay,
               int firstGregorianYear,
               int firstGregorianMonth,
               int firstGregorianDay)
  {
    this.lastJulianYear = lastJulianYear;
    this.lastJulianMonth = lastJulianMonth;
    this.lastJulianDay = lastJulianDay;
    this.firstGregorianYear = firstGregorianYear;
    this.firstGregorianMonth = firstGregorianMonth;
    this.firstGregorianDay = firstGregorianDay;

    if (compare (this.lastJulianYear,
                 this.lastJulianMonth, 
                 this.lastJulianDay,
                 this.firstGregorianYear,
                 this.firstGregorianMonth,
                 this.firstGregorianDay) >= 0) {
      throw new IllegalArgumentException ("GregorianCalendarGenerator compare");
    }

    if (this.lastJulianYear != this.firstGregorianYear) {
      if (this.lastJulianYear + 1 != this.firstGregorianYear) {
        throw new IllegalArgumentException
          ("GregorianCalendarGenerator lastJulianYear");
      }

      if (this.lastJulianMonth != 12) {
        throw new IllegalArgumentException
          ("GregorianCalendarGenerator lastJulianMonth");
      }

      if (this.lastJulianDay != 31) {
        throw new IllegalArgumentException
          ("GregorianCalendarGenerator lastJulianDay");
      }

      if (this.firstGregorianMonth != 1) {
        throw new IllegalArgumentException
          ("GregorianCalendarGenerator firstGregorianMonth");
      }

      lengthOfMonthInChangeoverYear = length();
      cumulativeDaysInChangeoverYear = cdays();

      LAST_JULIAN_DAY_OF_YEAR = 0;
    }
    else {
      lengthOfMonthInChangeoverYear = length();
      cumulativeDaysInChangeoverYear = cdays();

      LAST_JULIAN_DAY_OF_YEAR =
        cumulativeDaysInChangeoverYear[lastJulianMonth-1]
        + lastJulianDay;
    }


    firstDayOfMonthInChangeoverYear = first();
    lastDayOfMonthInChangeoverYear = last();


    J_LAST = date2jdn_julian (this.lastJulianYear,
                              this.lastJulianMonth,
                              this.lastJulianDay);

    this.numberOfDeletedDays =
      date2jdn_julian (this.firstGregorianYear,
                       this.firstGregorianMonth,
                       this.firstGregorianDay) - J_LAST - 1;
  }


  public final int getISOWeekNumber()
  {
    return getISOWeekNumber (getJulianDayNumber(),
                             getYear(),
                             firstGregorianYear + 1);
  }


  public boolean isDate (int year, int month, int day)
  {
    if ((year == lastJulianYear     && month == lastJulianMonth) ||
        (year == firstGregorianYear && month == firstGregorianMonth)) {
      //
      // Now we are in the year and month when calendar changed
      // from Julian to Gregorian.
      //
      if ((lastJulianYear  == firstGregorianYear) &&
          (lastJulianMonth == firstGregorianMonth)) {
        //
        // Calendar change was in the middle of the month, e.g. from
        // 4 October 1582 to 15 October 1582, so there is a hole
        // in the days from lastJulianDay to firstGregorianDay.
        //
        return ((1 <= day && day <= lastJulianDay) ||
                (firstGregorianDay <= day &&
                 day <= days_in_month (isLeapYear (year), month)));
      }
      else {
        //
        // We are either in lastJulianMonth or in firstGregorianMonth.
        // We need not know which one since arrays
        // (first|last)DayOfMonthInChangeoverYear
        // take care of first and last day of month.
        //
        return (firstDayOfMonthInChangeoverYear[month] <= day &&
                day <= lastDayOfMonthInChangeoverYear[month]);
      }
    }
    else {
      return ok_date (year, month, day);
    }
  }


  protected void jdn2date (int n)
  {
    if (n > J_LAST) {
      jdn2date_gregorian (n);
    }
    else {
      jdn2date_julian (n);
    }
    ok ("GregorianCalendarGenerator.jdn2date");
  }


  protected int date2jdn (int year, int month, int day)
  {
    if (compare (year, month, day,
                 lastJulianYear,
                 lastJulianMonth,
                 lastJulianDay) > 0) {
      return date2jdn_gregorian (year, month, day);
    }
    else {
      return date2jdn_julian (year, month, day);
    }
  }


  protected void doy2date (int year, int doy)
  {
    if (year == firstGregorianYear && doy > LAST_JULIAN_DAY_OF_YEAR) {
      doy2date (isLeapYear (year), year, doy + numberOfDeletedDays);
    }
    else {
      doy2date (isLeapYear (year), year, doy);
    }
  }


  protected int date2doy()
  {
    final int doy = date2doy (isLeapYear (getYear()), getMonth(), getDay());

    if (getYear() == firstGregorianYear && doy > LAST_JULIAN_DAY_OF_YEAR) {
      return (doy - numberOfDeletedDays);
    }
    else {
      return doy;
    }
  }


  protected int getCumulativeDays (int year, int month)
  {
    if (year == firstGregorianYear) {
      return cumulativeDaysInChangeoverYear[month];
    }
    else if (isLeapYear (year)) {
      return cumulative_days_in_leap_year[month];
    }
    else {
      return cumulative_days_in_ordinary_year[month];
    }
  }


  public boolean isLeapYear (int year)
  {
    if (year > firstGregorianYear) {
      return is_leap_year_gregorian (year);
    }
    else if (year == firstGregorianYear) {
      if (firstGregorianMonth == 1) {
        return is_leap_year_gregorian (year);
      }
      else if (firstGregorianMonth == 2) {
        return false;
      }
    }
    return is_leap_year_julian (year);
  }


  public int getLengthOfMonth (int year, int month)
  {
    if (year == firstGregorianYear) {
      return lengthOfMonthInChangeoverYear[month];
    }
    else {
      return days_in_month (isLeapYear (year), month);
    }
  }


  public int getFirstDayOfMonth (int year, int month)
  {
    if (year == firstGregorianYear) {
      return firstDayOfMonthInChangeoverYear[month];
    }
    else {
      return 1;
    }
  }


  public int getLastDayOfMonth (int year, int month)
  {
    if (year == firstGregorianYear) {
      return lastDayOfMonthInChangeoverYear[month];
    }
    else {
      return days_in_month (isLeapYear(year), month);
    }
  }


  private final int lastJulianYear;
  private final int lastJulianMonth;
  private final int lastJulianDay;
  private final int firstGregorianYear;
  private final int firstGregorianMonth;
  private final int firstGregorianDay;

  private final int numberOfDeletedDays;

  private final int[] lengthOfMonthInChangeoverYear;
  private final int[] cumulativeDaysInChangeoverYear;


  /**
   * Number of days from the start of changeover year to changeover day.
   */
  private final int LAST_JULIAN_DAY_OF_YEAR;


  /**
   * Julian day number of the last date of Julian calendar.
   * Next date is the first date of Gregorian calendar.
   */
  private final int J_LAST;


  private final int[] firstDayOfMonthInChangeoverYear;
  private final int[] lastDayOfMonthInChangeoverYear;


  /**
   * Calculate cumulative days in month in changeover year.
   */
  private final int[] cdays()
  {
    int d[] = new int[13];

    // Java initializes d[0] to zero.
    //
    for (int i = 1; i < d.length; i++) {
      d[i] = d[i-1] + lengthOfMonthInChangeoverYear[i];
    }

    return d;
  }


  /**
   * Calculate first day of months in changeover year.
   */
  private final int[] first()
  {
    int[] x = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    if (lastJulianMonth != firstGregorianMonth) {
      x[firstGregorianMonth] = firstGregorianDay;
    }

    return x;
  }


  /**
   * Calculate last day of months in changeover year.
   */
  private final int[] last()
  {
    int[] x = new int[13];

    for (int i = 1; i <= 12; i++) {
      x[i] = days_in_month (isLeapYear (firstGregorianYear), i);
    }


    if (lastJulianMonth != firstGregorianMonth) {
      x[lastJulianMonth] = lastJulianDay;
    }

    return x;
  }


  /**
   * Calculate length of months in changeover year.
   */
  private final int[] length()
  {
    int x[] = new int[13];

    for (int i = 1; i <= 12; i++) {
      x[i] = days_in_month (isLeapYear (firstGregorianYear), i);
    }

    if (lastJulianYear != firstGregorianYear) {
      x[firstGregorianMonth] -= firstGregorianDay - 1;
    }
    else if (lastJulianMonth == firstGregorianMonth) {
      x[firstGregorianMonth] -= firstGregorianDay - lastJulianDay - 1;
    }
    else {
      x[lastJulianMonth] = lastJulianDay;
      x[firstGregorianMonth] -= firstGregorianDay - 1;
    }

    return x;
  }
}
