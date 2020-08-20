/* fi/joensuu/joyds1/calendar/GregorianCalendar.java
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
 * Gregorian calendar.<p>
 *
 * Calendar changed from Julian to Gregorian in 1582,
 * and 4 October 1582 was followed by 15 October 1582.
 */
public class GregorianCalendar extends GregorianCalendarGenerator {
  /**
   * Construct a date from current time, using
   * the default time zone and locale.
   */
   public GregorianCalendar()
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
  public GregorianCalendar (int year, int month, int day)
  {
    super (1582, 10, 4, 1582, 10, 15);
    set (year, month, day);
  }


  /**
   * Construct a date from year and day of year.
   *
   * @param year  Year.
   * @param doy   Day of year. First day of year is 1.
   */
  public GregorianCalendar (int year, int doy)
  {
    super (1582, 10, 4, 1582, 10, 15);
    set (year, doy);
  }


  /**
   * Construct a date from Julian day number.
   *
   * @param jd Julian day number.
   */
  public GregorianCalendar (int jd)
  {
    super (1582, 10, 4, 1582, 10, 15);
    set (jd);
  }


  /**
   * Construct a date from Calendar.
   *
   * @param c Date to be assigned.
   */
  public GregorianCalendar (Calendar c)
  {
    super (1582, 10, 4, 1582, 10, 15);
    set (c);
  }


  /**
   * Construct a date from java.util.GregorianCalendar.
   *
   * @param c Date to be assigned.
   */
  public GregorianCalendar (java.util.GregorianCalendar c)
  {
    super (1582, 10, 4, 1582, 10, 15);
    set (c);
  }
}
