/* fi/joensuu/joyds1/calendar/CalendarFromString.java
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

package fi.joensuu.joyds1.calendar;


/**
 * This class has static functions to convert strings to calendar classes.<p>
 *
 * Calendar name can be one of
 *
 * <ul>
 * <li> ArmenianCalendar
 * <li> BahaiCalendar
 * <li> BohemianMoravianCalendar
 * <li> BritishCalendar
 * <li> BulgarianCalendar
 * <li> ChineseCalendar
 * <li> CopticCalendar
 * <li> DanishCalendar
 * <li> EgyptianCalendar
 * <li> EthiopicCalendar
 * <li> FinnishCalendar
 * <li> FrenchRevolutionaryCalendar
 * <li> GregorianCalendar
 * <li> HebrewCalendar
 * <li> HinduLunarCalendar
 * <li> HinduSolarCalendar
 * <li> HungarianCalendar
 * <li> IslamicCalendar
 * <li> JalaliCalendar
 * <li> JulianCalendar
 * <li> LuxemburgianCalendar
 * <li> MayanCalendar
 * <li> NepaliCalendar
 * <li> PersianCalendar
 * <li> PrussianCalendar
 * <li> RomanianCalendar
 * <li> RussianCalendar
 * <li> SwissProtestantCalendar
 * <li> WorldCalendar
 * </ul>
 *
 * You can leave out the word Calendar. For example you can use either
 * <code>GregorianCalendar</code> <code>Gregorian</code>.<p>
 *
 * {@link #getInstance(String)} throws an exception on error and
 * {@link #getCalendar(String)} returns <code>null</code> on error.
 */
public final class CalendarFromString {
  /**
   * Convert class name to class and throw an exception on error.
   *
   * @param calendarName Class name to be converted.
   *
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   *
   * @return The class.
   */
  public static final Calendar getInstance (String calendarName)
    throws ClassNotFoundException,
           InstantiationException,
           IllegalAccessException,
           IllegalArgumentException
  {
    calendarName = getDepackagedCalendarName (calendarName);

    if (!calendarName.endsWith ("Calendar")) {
      calendarName += "Calendar";
    }

    if (java.util.Arrays.binarySearch (calendar, calendarName) < 0) {
      throw new IllegalArgumentException (calendarName);
    }

    calendarName= "fi.joensuu.joyds1.calendar." + calendarName;

    return (Calendar)Class.forName(calendarName).newInstance();
  }


  /**
   * Convert class name to class and return <code>null</code> on error.
   *
   * @param calendarName Calendar name to be converted.
   *
   * @return The class.
   */
  public static final Calendar getCalendar (String calendarName)
  {
    try {
      return getInstance (calendarName);
    }
    catch (Throwable t)
    {
//System.out.println ("Hupsis " + calendarName + ": " + t.toString());
      return null;
    }
  }


  /**
   * Convert localized calendar name to class and return <code>null</code> on error.
   *
   * @param calendarName Localized calendar name to be converted.
   *
   * @return The class or null if there is no calendar corresponding to calendarName.
   */
  public static final Calendar getCalendarFromLocalizedName (String calendarName)
  {
    // We must use linear search since localized calendar names
    // may not be in alphabetical order.
    //
    for (int i = 0; i < calendar.length; i++) {
      if (localizedCalendar[i].compareTo (calendarName) == 0) {
        return CalendarFromString.getCalendar (calendar[i]);
      }
    }
    return null;
  }


  /**
   * Returns the names of the calendars this class supports.
   */
  public static final String[] getCalendarNames()
  {
    return calendar;
  }


  /**
   * Returns the localized names for the calendars this class supports.
   * The names may not be in alphabetical order.
   */
  public static final String[] getLocalizedCalendarNames()
  {
    return localizedCalendar;
  }

    
  /*
   * This list was made with the command
   *
   * ls -1 *Calendar.java | gawk -F. '{print "  \"" $1 "\","}'
   *
   * and extra lines were deleted.
   */
  private static final String[] calendar = {
    "ArmenianCalendar",
    "BahaiCalendar",
    "BohemianMoravianCalendar",
    "BritishCalendar",
    "BulgarianCalendar",
    "ChineseCalendar",
    "CopticCalendar",
    "DanishCalendar",
    "EgyptianCalendar",
    "EthiopicCalendar",
    "FinnishCalendar",
    "FrenchRevolutionaryCalendar",
    "GregorianCalendar",
    "HebrewCalendar",
    "HinduLunarCalendar",
    "HinduSolarCalendar",
    "HungarianCalendar",
    "IslamicCalendar",
    "JalaliCalendar",
    "JulianCalendar",
    "LuxemburgianCalendar",
    "MayanCalendar",
    "NepaliCalendar",
    "PersianCalendar",
    "PrussianCalendar",
    "RomanianCalendar",
    "RussianCalendar",
    "SwissProtestantCalendar",
    "WorldCalendar",
  };

  private static final String[] localizedCalendar = initLocalizedCalendarNames();


  /**
   * This class is non-instantiable.
   */
  private CalendarFromString()
  {
  }


  private static final String[] initLocalizedCalendarNames()
  {
    String[] s = new String[calendar.length];

    for (int i = 0; i < calendar.length; i++) {
      s[i] = Resources.getString (calendar[i]);
    }

    return s;
  }


  /**
   * Delete package name from calendar name and return it. For example, if
   * <code>calendarName</code> is
   * <code>fi.joensuu.joyds1.calendar.GregorianCalendar</code>
   * this function returns
   * <code>GregorianCalendar</code>.<p>
   *
   * This function does not check that <code>calendarName</code> is a name
   * of some calendar. It just finds the last period (.) in the string
   * <code>calendarName</code> and returns all characters after that to
   * the end of the string. If <code>calendarName</code> has no periods
   * this function returns <code>calendarName</code>.
   */
  public static final String getDepackagedCalendarName (String calendarName)
  {
    final int n = calendarName.lastIndexOf ('.');

    if (n >= 0) {
      return calendarName.substring (n+1);
    }
    else {
      return calendarName;
    }
  }


  /**
   * Get calendar name without the package name.<p>
   *
   * For example, if the runtime type of <code>calendar</code> is
   * <code>fi.joensuu.joyds1.calendar.GregorianCalendar</code>
   * this function returns the string
   * <code>GregorianCalendar</code>.
   */
  public static final String getDepackagedCalendarName (Calendar calendar)
  {
    return getDepackagedCalendarName (calendar.getClass().getName());
  }
}
