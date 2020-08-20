/* fi/joensuu/joyds1/calendar/YMD.java
   Copyright (C) 2004 Hannu V_is_nen
 
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
 * This class has functions for
 * calendars that divide time into years, months and days.
 */
abstract class YMD extends Calendar
{
  /**
   * Get the value of year.
   */
  public final int getYear() {return year;}


  /**
   * Get the value of month. First month of year is 1.
   */
  public final int getMonth() {return month;}


  /**
   * Get the value of day. First day of month is 1.
   */
  public final int getDay() {return day;}


  /**
   * Return date as a string of a form <code>year-month-day</code>.<p>
   *
   * For example, <code>2000-1-23</code>.<p>
   *
   * This method is primarily for debugging and for quick-and-dirty output.
   * For real date formatting, use
   * {@link fi.joensuu.joyds1.calendar.format.DateFormat DateFormat} and
   * {@link fi.joensuu.joyds1.calendar.format.SimpleDateFormat SimpleDateFormat}.
   */
  public String toString()
  {
    return (getYear() + "-" + getMonth() + "-" + getDay());
  }


  /**
   * Year.
   */
  protected int year;

  /**
   * Month.
   */
  protected int month;

  /**
   * Day.
   */
  protected int day;
}
