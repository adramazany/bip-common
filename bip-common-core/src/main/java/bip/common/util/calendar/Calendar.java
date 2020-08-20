/* fi/joensuu/joyds1/calendar/Calendar.java
   Copyright (C) 2003-2005 Hannu V_is_nen
 
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

import java.util.Locale;


/**
 * Abstract base class for calendars.<p>
 *
 * Dates are represented internally as Julian day number.
 * See {@link #getJulianDayNumber}.
 *
 * Subclasses of this class implement different calendars.<p>
 *
 * I define calendar as a set of rules for manipulating dates. :-)<p>
 *
 * Functions like
 * <code>getYear()</code>,
 * <code>getMonth()</code> and
 * <code>getDay()</code> return
 * different components of the date. If they are not defined
 * for a calendar they throw
 * <code>UnsupportedOperationException</code>.
 * For example, MayanCalendar has no concept of year.<p>
 *
 * This class has a different <code>get</code> function for each
 * date component  whereas <code>java.util.Calendar</code> has only
 * one function <code>int get (int field)</code>.<p>
 *
 * This class defines symbolic names for weekdays (MONDAY, etc)
 * and months (JANUARY, etc). The names are the same as in
 * java.util.Calendar but the values may be different.
 */
public abstract class Calendar
  implements Cloneable, Comparable, java.io.Serializable
{
  /**
   * Get cycle component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getCycle()
  {
    throw new UnsupportedOperationException ("getCycle");
  }


  /**
   * Get year component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getYear()
  {
    throw new UnsupportedOperationException ("getYear");
  }
 
 
  /**
   * Get month component of this date. First month of year is 1.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getMonth()
  {
    throw new UnsupportedOperationException ("getMonth");
  }
 
 
  /**
   * Get day component of this date. First day of month is 1.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getDay()
  {
    throw new UnsupportedOperationException ("getDay");
  }


  /**
   * Get Julian day number of this date.<p>
   *
   * 1 January 4713 BC is Julian day number 0 as defined in the
   * <a href="http://www.tondering.dk/claus/calendar.html">Calendar
   * FAQ</a>.
   */
  public int getJulianDayNumber()
  {
    return jdn;
  }


  /**
   * Get day of week of this date (MONDAY, etc).<p>
   *
   * If the calendar has no weeks, this function may return a number
   * in a named interval that is greater than a day and smaller than a
   * month, like d�cade in {@link FrenchRevolutionaryCalendar}.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getDayOfWeek()
  {
    throw new UnsupportedOperationException ("getDayOfWeek");
  }


  /**
   * Get
   * <a href="hhttp://www.tondering.dk/claus/cal/node7.html#SECTION00770000000000000000">
   * ISO week number</a> of this date, according to standard
   * ISO 8601:1988.<p>
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getISOWeekNumber()
  {
    throw new UnsupportedOperationException ("getISOWeekNumber");
  }


   /**
   * Get ISO year of this date corresponding to ISO week number,
   * according to standard ISO 8601:1988.<p>
   *
   * See {@link #getISOWeekNumber()}.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getISOYear()
  {
    throw new UnsupportedOperationException ("getISOYear");
  }


  /**
   * Get day of year of this date. First day of year is 1.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getDayOfYear()
  {
    throw new UnsupportedOperationException ("getDayOfYear");
  }


  /**
   * Is this year (i.e. getYear()) a leap year?
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public boolean isLeapYear()
  {
    throw new UnsupportedOperationException ("isLeapYear");
  }


  /**
   * Return true if this month is a leap month?
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public boolean isLeapMonth()
  {
    throw new UnsupportedOperationException ("isLeapMonth");
  }


  /**
   * Is (year, month, day) a legal date in this calendar?
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public boolean isDate (int year, int month, int day)
  {
    throw new UnsupportedOperationException
                ("isDate (int year, int month, int day)");
  }


  /**
   * Is (year, month, day, leapMonth) a legal date in this calendar?
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public boolean isDate (int year, int month, int day, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("isDate (int year, int month, int day, boolean leapMonth)");
  }


  /**
   * Is (cycle, year, month, day, leapMonth) is a legal date in this calendar?
   */
  public boolean isDate (int cycle, int year, int month, int day, boolean leapMonth)
  {
   throw new UnsupportedOperationException
                ("isDate (int cycle, int year, int month, int day, boolean leapMonth)");
  }


  /**
   * Is (year, doy) a legal date in this calendar?
   *
   * @param year Year.
   * @param doy  Day of year. First day of year is 1.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public boolean isDate (int year, int doy)
  {
    throw new UnsupportedOperationException
               ("isDate (int year, int doy)");
  }


  /**
   * Set this date to (year, month, day).
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public void set (int year, int month, int day)
  {
    throw new UnsupportedOperationException
                ("set (int year, int month, int day)");
  }


  /**
   * Set this date to (year, month, day, leapMonth).
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public void set (int year, int month, int day, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("set (int year, int month, int day, boolean leapMonth)");
  }


  /**
   * Set this date to (cycle, year, month, day, leapMonth).
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public void set (int cycle, int year, int month, int day, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("set (int cycle, int year, int month, int day, boolean leapMonth)");
  }


  /**
   * Set this date to (year, doy).
   *
   * @param year Year.
   * @param doy  Day of year. First day of year is 1.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public void set (int year, int doy)
  {
    throw new UnsupportedOperationException
                ("set (int year, int doy)");
  }


  /**
   * Set this date to c.
   *
   * @param c Date to be assigned.
   */
  public void set (java.util.GregorianCalendar c)
  {
    GregorianCalendar g = new GregorianCalendar
                            (c.get (java.util.Calendar.YEAR),
                             c.get (java.util.Calendar.MONTH) + 1,
                             c.get (java.util.Calendar.DATE));
 
    set (g.getJulianDayNumber()); // Convert to correct calendar.
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
    ok ("Calendar set (int jd)");
  }


  /**
   * Set this date to c.
   *
   * @param c Date to be assigned.
   */
  public void set (Calendar c)
  {
    set (c.getJulianDayNumber());
  }


  /**
   * Compare to dates.<p>
   *
   * Dates are compared by comparing the Julian day number.
   *
   * @param obj Date to be compared.
   *
   * @return 0, if the date represented by obj is same than this date,
   *         negative if this date is before the other date,
   *         and positive otherwise
   *
   * @exception ClassCastException if obj is not of type Date.
   */
  public int compareTo (Object obj)
  {
    return compareTo ((Calendar)obj);
  }


  /**
   * Compare this date to another.<p>
   *
   * Dates are compared by comparing the Julian day number.
   *
   * @param c Date to be compared.
   *
   * @return 0, if the date represented by c is same than this date,
   *         negative if this date is before the other date,
   *         and positive otherwise.
   */
  public int compareTo (Calendar c)
  {
    return (getJulianDayNumber() - c.getJulianDayNumber());
  }


  /**
   * Add days to date.
   *
   * @param days Number of days to be added.
   *             If <code>days</code> is negative, days are subtracted.
   */
  public void addDays (int days)
  {
    set (getJulianDayNumber() + days);
  }


  /**
   * Convert date to string.
   */
  public abstract String toString();


  /**
   * Get today's date using current time zone and locale.<p>
   *
   * Run time type of the return value is
   * {@link GregorianCalendar}.
   */
  public static final Calendar getToday()
  {
    java.util.Calendar d = new java.util.GregorianCalendar();

    return new GregorianCalendar
                 (d.get (java.util.Calendar.YEAR),
                  d.get (java.util.Calendar.MONTH) + 1,
                  d.get (java.util.Calendar.DATE));
  }


  /**
   * Return a clone of this object.
   */
  public Object clone()
  {
    try {
      return (Calendar) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      return null;
    }
  }


  /**
   * Convert this date to object of type java.util.GregorianCalendar.
   */
  public java.util.GregorianCalendar toJavaUtilGregorianCalendar()
  {
    GregorianCalendar g = new GregorianCalendar (getJulianDayNumber());
    return new java.util.GregorianCalendar
                 (g.getYear(), g.getMonth()-1, g.getDay(), 0, 0, 0);
  }


  /**
   * Compare the given date with this.
   *
   * @param o the object to that we should compare.
   *
   * @return true, if the given object is a date, that represents
   * the same Julian day number (but doesn't necessary have the same fields).
   *
   * @XXX Should we check if year, month, day are equal?
   * @XXX Code is from GNU Classpath's java.util.Calendar.
   */
  public boolean equals (Object o)
  {
    return ((o instanceof Calendar)
            && getJulianDayNumber() == ((Calendar)o).getJulianDayNumber());
  }


  /**
   * Return a hash code for this calendar.
   *
   * @return a hash code, which fullfits the general contract of
   * <code>hashCode()</code><p>
   *
   * Code is inspired by GNU Classpath's java.util.Calendar.
   */
  public int hashCode()
  {
    final long jdn = (long)getJulianDayNumber();
    return (int) ((jdn & 0xffffffffL) ^ (jdn >> 32));
  }


  /**
   * Test if this is a legal date and throw IllegalArgumentException
   * if it is not.
   */
  protected abstract boolean ok (String message);


  /**
   * Convert Julian day number to date.
   *
   * @param n Julian day number.
   */
  protected abstract void jdn2date (int n);


  /**
   * Convert date to Julian day number.
   */
  protected abstract int date2jdn();


  /**
   * Is year leap year?
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public boolean isLeapYear (int year)
  {
    throw new UnsupportedOperationException ("isLeapYear (int year)");
  }


  /**
   * Return length of year.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLengthOfYear (int year)
  {
    throw new UnsupportedOperationException ("getLengthOfYear (int year)");
  }


  /**
   * Return length of month.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLengthOfMonth (int year, int month)
  {
    throw new UnsupportedOperationException
                ("getLengthOfMonth (int year, int month");
  }


  /**
   * Return length of month.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLengthOfMonth (int year, int month, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("getLengthOfMonth (int year, int month, boolean leapMonth");
  }


  /**
   * Return first day of month.<p>
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getFirstDayOfMonth (int year, int month)
  {
    throw new UnsupportedOperationException
                ("getFirstDayOfMonth (int year, int month)");
  }


  /**
   * Return first day of month.<p>
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getFirstDayOfMonth (int year, int month, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("getFirstDayOfMonth (int year, int month, boolean leapMonth)");
  }


  /**
   * Return last day of month.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLastDayOfMonth (int year, int month)
  {
    throw new UnsupportedOperationException
                ("getLastDayOfMonth (int year, int month)");
  }


  /**
   * Return last day of month.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLastDayOfMonth (int year, int month, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("getLastDayOfMonth (int year, int month, boolean leapMonth)");
  }


  /**
   * Return first month of year.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getFirstMonthOfYear (int year)
  {
    throw new UnsupportedOperationException ("getFirstMonthOfYear (int year)");
  }


  /**
   * Return last month of year.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLastMonthOfYear (int year)
  {
    throw new UnsupportedOperationException ("getLastMonthOfYear (int year)");
  }


  /**
   * Return length of year.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLengthOfYear (int cycle, int year)
  {
    throw new UnsupportedOperationException ("getLengthOfYear (int cycle, int year)");
  }


  /**
   * Return length of month.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLengthOfMonth (int cycle, int year, int month, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("getLengthOfMonth (int cycle, int year, int month, boolean leapMonth");
  }


  /**
   * Return first day of month.<p>
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getFirstDayOfMonth (int cycle, int year, int month, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("getFirstDayOfMonth (int cycle, int year, int month, boolean leapMonth)");
  }


  /**
   * Return last day of month.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLastDayOfMonth (int cycle, int year, int month, boolean leapMonth)
  {
    throw new UnsupportedOperationException
                ("getLastDayOfMonth (int cycle, int year, int month, boolean leapMonth)");
  }


  /**
   * Return first month of year.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getFirstMonthOfYear (int cycle, int year)
  {
    throw new UnsupportedOperationException ("getFirstMonthOfYear (int cycle, int year)");
  }


  /**
   * Return last month of year.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getLastMonthOfYear (int cycle, int year)
  {
    throw new UnsupportedOperationException ("getLastMonthOfYear (int cycle, int year)");
  }


  /**
   * Return the number of days from start of year to end of month.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  protected int getCumulativeDays (int year, int month)
  {
    throw new UnsupportedOperationException ("getCumulativeDays (int year, int month)");
  }


  /**
   * Convert day of year to date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  protected void doy2date (int year, int doy)
  {
    throw new UnsupportedOperationException ("doy2date (int year, int doy)");
  }


  /**
   * Convert (year, month, day) to Julian day number.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  protected int date2jdn (int year, int month, int day)
  {
    throw new UnsupportedOperationException ("date2jdn (int year, int month, int day)");
  }



  /**
   * Julian day number.
   */
  protected int jdn;


  /**
   * Get baktun component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getBaktun()
  {
    throw new UnsupportedOperationException ("getBaktun");
  }


  /**
   * Get katun component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getKatun()
  {
    throw new UnsupportedOperationException ("getKatun");
  }


  /**
   * Get tun component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getTun()
  {
    throw new UnsupportedOperationException ("getTun");
  }


  /**
   * Get uinal component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getUinal()
  {
    throw new UnsupportedOperationException ("getUinal");
  }


  /**
   * Get kin component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getKin()
  {
    throw new UnsupportedOperationException ("getKin");
  }


  /**
   * Is (baktun, katun, tun, uinal, kin) a legal date in this calendar?
   *
   * @param baktun
   * @param katun
   * @param tun
   * @param uinal
   * @param kin
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public boolean isDate (int baktun,
                         int katun,
                         int tun,
                         int uinal,
                         int kin)
  {
    throw new UnsupportedOperationException ("isDate (int baktun, ...");
  }


  /**
   * Set this date to (baktun, katun, tun, uinal, kin).
   *
   * @param baktun
   * @param katun
   * @param tun
   * @param uinal
   * @param kin
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public void set (int baktun,
                   int katun,
                   int tun,
                   int uinal,
                   int kin)
  {
    throw new UnsupportedOperationException ("set (int baktun, ...");
  }


  /**
   * Get haabDay component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getHaabDay()
  {
    throw new UnsupportedOperationException ("getHaabDay");
  }


  /**
   * Get haabMonth component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getHaabMonth()
  {
    throw new UnsupportedOperationException ("getHaabMonth");
  }


  /**
   * Get tzolkinDay component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getTzolkinDay()
  {
    throw new UnsupportedOperationException ("getTzolkinDay");
  }


  /**
   * Get tzolkinMonth component of this date.
   *
   * @throws UnsupportedOperationException
   *         if operation is not supported by this calendar.
   */
  public int getTzolkinMonth()
  {
    throw new UnsupportedOperationException ("getTzolkinMonth");
  }


  /**
   * Get first Julian day of this calendar. That is, get the Julian day
   * number for the start date of this calendar.
   */
  public abstract int getFirstJulianDay();


  /**
   * Get first year of this calendar.
   *
   * @throws UnsupportedOperationException
   *         if <code>getYear()</code> is not supported by this calendar.
   */
  public final int getFirstYear()
  {
    Calendar c = (Calendar)clone();
    c.set (getFirstJulianDay());
    return c.getYear();
  }


  /**
   * Positive remainder of m/n (i.e. m % n) with n instead of 0.<p>
   *
   * For example: <code>amod(4,4)</code> returns 4, not 0.<p>
   */
  protected static final int amod (int m, int n)
  {
    return (1 + ((m - 1) % n));
  }


  /**
   * Convert Julian date to Julian day number.<p>
   *
   * This function does not check that (year, month, day) is a correct
   * date in Julian calendar.
   *
   * @param year  Year.
   * @param month Month.
   * @param day   Day.
   * @return Julian day number.
   */
  protected static final int date2jdn_julian (int year, int month, int day)
  {
    // From the Calendar FAQ.
    //
    int a = (14 - month) / 12;
    int y = year + 4800 - a;
    int m = month + 12*a - 3;

    return (day + (153*m+2)/5 + y*365 + y/4 - 32083);
  }


  /**
   * Convert Gregorian date to Julian day number.<p>
   *
   * This function does not check that (year, month, day) is a correct
   * date in Gregorian calendar.
   *
   * @param year  Year.
   * @param month Month.
   * @param day   Day.
   * @return Julian day number.
   */
  protected static final int date2jdn_gregorian (int year, int month, int day)
  {
    // From the Calendar FAQ.
    //
    int a = (14 - month) / 12;
    int y = year + 4800 - a;
    int m = month + 12*a - 3;

    return (day + (153*m+2)/5 + y*365 + y/4 - y/100 + y/400 - 32045);
  }


  /**
   * Compare two dates.<p>
   * This function assumes that the year starts on 1 January and
   * ends on 31 December.<p>
   *
   * @return A negative integer, zero, or a positive integer as
   *         (year1, month1, day1) is less than, equal to, or
   *         greater than (year2, month2, day2).
   */
  static final int compare (int year1, int month1, int day1,
                            int year2, int month2, int day2)
  {
    if (year1 < year2)
      return -1;
    else if (year1 > year2)
      return 1;
    else if (month1 < month2)
      return -1;
    else if (month1 > month2)
      return 1;
    else
      return (day1 - day2);
  }


  /**
   * Return the first date of <code>c</code>.
   *
   * @param calendar Calendar whose start date is to be returned.
   *
   * @return The start date of calendar. The runtime type of the
   *         return value is the same than c's runtime type.<p>
   *
   *         If runtime type of calendar is MayanCalendar, the
   *         first date is Goodman-Martinez-Thompson's value.
   */
  public static final Calendar getFirstDate (Calendar calendar)
  {
    if (calendar.getClass().getName().compareTo
         ("fi.joensuu.joyds1.calendar.MayanCalendar") == 0) {
      return new MayanCalendar (MayanCalendar.FIRST_JULIAN_DAY);
    }
    else {
      Calendar k = (Calendar)calendar.clone();
      k.set (calendar.getFirstJulianDay());
      return k;
    }
  }


  /**
   * Modulo function for doubles.<p>
   *
   * <code>mod(x,y)</code> returns <code>x - y*Math.floor(x/y)</code>.
   */
  protected static final double mod (double x, double y)
  {
    return (x - y*Math.floor(x/y));
  }


  /**
   * Constant representing Monday.
   */
  public static final int MONDAY = 1;


  /**
   * Constant representing Tuesday.
   */
  public static final int TUESDAY = 2;


  /**
   * Constant representing Wednesday.
   */
  public static final int WEDNESDAY = 3;


  /**
   * Constant representing Thursday.
   */
  public static final int THURSDAY = 4;


  /**
   * Constant representing Friday.
   */
  public static final int FRIDAY = 5;


  /**
   * Constant representing Saturday.
   */
  public static final int SATURDAY = 6;


  /**
   * Constant representing Sunday.
   */
  public static final int SUNDAY = 7;


  /**
   * Constant representing the first month of the year.
   */
  public static final int JANUARY = 1;


  /**
   * Constant representing the second month of the year.
   */
  public static final int FEBRUARY = 2;


  /**
   * Constant representing the third month of the year.
   */
  public static final int MARCH = 3;


  /**
   * Constant representing the fourth month of the year.
   */
  public static final int APRIL = 4;


  /**
   * Constant representing the fift month of the year.
   */
  public static final int MAY = 5;


  /**
   * Constant representing the sixth month of the year.
   */
  public static final int JUNE = 6;


  /**
   * Constant representing the seventh month of the year.
   */
  public static final int JULY = 7;


  /**
   * Constant representing the eight month of the year.
   */
  public static final int AUGUST = 8;


  /**
   * Constant representing the ninth month of the year.
   */
  public static final int SEPTEMBER = 9;


  /**
   * Constant representing the tenth month of the year.
   */
  public static final int OCTOBER = 10;


  /**
   * Constant representing the eleventh month of the year.
   */
  public static final int NOVEMBER = 11;


  /**
   * Constant representing the twelft month of the year.
   */
  public static final int DECEMBER = 12;


  /**
   * Constant representing the thirteenth month of the year.
   * This is an artificial name useful for lunar calendars.
   */
  public static final int UNDECIMBER = 13;


  /**
   * Get default calendar for locale.<p>
   *
   * Default calendar for a locale is defined by property DefaultCalendar
   * in file ResourceBundle*.properties.
   *
   * @return Calendar for locale.
   * @throws RuntimeException if something goes wrong; e.g. if
   *                          resource file has no property DefaultCalendar.
   */
  public static final Calendar getInstance (Locale locale)
  {
    try {
////System.out.println ("Calendar " + locale.toString());
      return CalendarFromString.getCalendar
               (Resources.getBundle(locale).getString ("DefaultCalendar"));
    }
    catch (Throwable t)
    {
      throw new RuntimeException ("getInstance(Locale)", t);
//      return new GregorianCalendar();
    }
  }


  /**
   * Get default calendar for default locale.<p>
   *
   * @return Calendar for default locale.
   * @throws RuntimeException if something goes wrong.
   *
   * @see #getInstance(Locale)
   */
  public static final Calendar getInstance()
  {
    try {
      return CalendarFromString.getInstance
               (Resources.getString ("DefaultCalendar"));
    }
    catch (Throwable t)
    {
      throw new RuntimeException ("getInstance", t);
//    return new GregorianCalendar();
    }
  }
}
