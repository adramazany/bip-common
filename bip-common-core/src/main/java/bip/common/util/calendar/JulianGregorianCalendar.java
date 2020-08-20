/* fi/joensuu/joyds1/calendar/JulianGregorianCalendar.java
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

/**
 * Abstract base class for Julian and Gregorian calendars.<p>
 *
 * This class also contains static methods for Julian and
 * Gregorian calendars.<p>
 *
 * They are mostly from the
 * <a href="http://www.tondering.dk/claus/calendar.html">Calendar
 * FAQ</a>.
 */
abstract class JulianGregorianCalendar extends OldWorldCalendar
{
  public final int getISOYear()
  {
    // Since ISO year is defined only if ISO week number is defined
    // we assume that getISOWeekNumber checks that they are defined
    // for this calendar.

    final int W = getISOWeekNumber();

    if ((W > 50) && (getMonth() == 1)) {
      return (getYear() - 1);
    }
    else if ((W < 10) && (getMonth() == 12)) {
      return (getYear() + 1);
    }
    else {
      return getYear();
    }
  }


  /**
   * Is (year, doy) a legal date in this calendar?
   *
   * @param year Year.
   * @param doy  Day of year. First day of year is 1.
   */
  public boolean isDate (int year, int doy)
  {
    return ((year >= FIRST_YEAR)
            && (1 <= doy && doy <= getLengthOfYear (year)));
  }


  /**
   * Return last month of year.
   */
  public int getLastMonthOfYear (int year)
  {
    return 12;
  }


  /**
   * Convert day of year to date.
   */
  protected final void doy2date (boolean isLeapYear, int year, int doy)
  {
    this.year = year;
    if (isLeapYear) {
      month = doy2month_in_leap_year[doy];
      day   = doy2day_in_leap_year[doy];
    }
    else {
      month = doy2month_in_ordinary_year[doy];
      day   = doy2day_in_ordinary_year[doy];
    }
  }


  /**
   * Is year a leap year in Gregorian calendar?
   */
  protected static final boolean is_leap_year_gregorian  (int year)
  {
    // Algorithm: Kernighan & Richie,
    // "The C Programming Language", 1st edition (1978), p. 37.
    //
    return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
  }


  /**
   * Is year a leap year in Julian calendar?
   */
  protected static final boolean is_leap_year_julian  (int year)
  {
    return (year % 4 == 0);
  }


  /**
   * Convert date to day of year.
   */
  protected static final int date2doy
    (boolean isLeapYear, int month, int day)
  {
    return (isLeapYear
            ? date2doy_in_leap_year[month][day]
            : date2doy_in_ordinary_year[month][day]);
  }


  /**
   * Convert date to day of year.
   */
  protected static final int date2doy 
    (boolean isLeapYear, int month, int day, int number_of_deleted_days)
  {
    return (date2doy (isLeapYear, month, day) - number_of_deleted_days);
  }



  /**
   * Return number of days in month.
   */
  protected static final int days_in_month (boolean isLeapYear, int month)
  {
    return (isLeapYear
            ? days_in_month_in_leap_year[month]
            : days_in_month_in_ordinary_year[month]);
  }


  /**
   * Return true if year >= FIRST_YEAR and
   * month is between 1 and last month of year inclusive and
   * day is between 1 and length of month inclusive.
   */
  protected final boolean ok_date (int year, int month, int day)
  {
    return ((year >= FIRST_YEAR) &&
            (1 <= month && month <= getLastMonthOfYear(year)) &&
            (1 <= day && day <= getLengthOfMonth (year, month)));
  }



  /**
   * Get ISO week number of this date, according to standard
   * ISO 8601:1988.<p>
   *
   * The standard defines the week number for Gregorian calendars only
   * and this function should be called only on full Gregorian years.<p>
   *
   * For example, Finland changed to Gregorian calendar in 1753 when
   * 17 February 1753 was followed by 1 March 1753, and thus the
   * ISO week number is defined in 1754 and afterwards.<p>
   *
   * Algorithm is from the
   * <a href="hhttp://www.tondering.dk/claus/cal/node7.html#SECTION00770000000000000000">
   * Calendar FAQ</a>.<p>
   *
   * @param J    Julian day number of a date the ISO week number is to be calculated.
   * @param year Year of date the ISO week number is to be calculated.
   * @param full First full Gregorian year for this calendar.
   *
   * @throws IllegalArgumentException if year < full
   */
  protected static final int getISOWeekNumber (int J, int year, int full)
  {
    if (year < full) {
      throw new IllegalArgumentException
        ("getISOWeekNumber: year < full");
    }

    int d4 = (((J + 31741 - (J % 7)) % 146097) % 36524) % 1461;
    int L  = d4 / 1460;
    int d1 = ((d4 - L) % 365) + L;
    return ((d1/7) + 1);
  }


  private static final int FIRST_YEAR = -4712; // 4713 B.C.


  /**
   * Cumulative number of days from the start of the year to the
   * end of month (1 <= month <= 12) in leap years.<p>
   *
   * January = 31, February = 60, etc.
   */
  protected static final int cumulative_days_in_leap_year[] = {
    0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366
  };


  /**
   * Cumulative number of days from the start of the year to the
   * end of month (1 <= month <= 12) in non-leap years.<p>
   *
   * January = 31, February = 59, etc.
   */
  protected static final int cumulative_days_in_ordinary_year[] = {
    0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365
  };
 

  /**
   * Number of days in month in leap years (1 <= month <= 12).<p>
   *
   * January = 31, February = 29, etc.
   */
  protected static final int days_in_month_in_leap_year[] = {
    0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
  };


  /**
   * Number of days in month in non-leap years (1 <= month <= 12).<p>
   *
   * January = 31, February = 28, etc.
   */
  protected static final int days_in_month_in_ordinary_year[] = {
    0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
  };


  /**
   * Table that converts day of year to month in leap years.
   */
  protected static final int doy2month_in_leap_year[] = {
     0,  1,  1,  1,  1,  1,  1,  1,  1,  1,
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
     1,  1,  2,  2,  2,  2,  2,  2,  2,  2,
     2,  2,  2,  2,  2,  2,  2,  2,  2,  2,
     2,  2,  2,  2,  2,  2,  2,  2,  2,  2,
     2,  3,  3,  3,  3,  3,  3,  3,  3,  3,
     3,  3,  3,  3,  3,  3,  3,  3,  3,  3,
     3,  3,  3,  3,  3,  3,  3,  3,  3,  3,
     3,  3,  4,  4,  4,  4,  4,  4,  4,  4,
     4,  4,  4,  4,  4,  4,  4,  4,  4,  4,
     4,  4,  4,  4,  4,  4,  4,  4,  4,  4,
     4,  4,  5,  5,  5,  5,  5,  5,  5,  5,
     5,  5,  5,  5,  5,  5,  5,  5,  5,  5,
     5,  5,  5,  5,  5,  5,  5,  5,  5,  5,
     5,  5,  5,  6,  6,  6,  6,  6,  6,  6,
     6,  6,  6,  6,  6,  6,  6,  6,  6,  6,
     6,  6,  6,  6,  6,  6,  6,  6,  6,  6,
     6,  6,  6,  7,  7,  7,  7,  7,  7,  7,
     7,  7,  7,  7,  7,  7,  7,  7,  7,  7,
     7,  7,  7,  7,  7,  7,  7,  7,  7,  7,
     7,  7,  7,  7,  8,  8,  8,  8,  8,  8,
     8,  8,  8,  8,  8,  8,  8,  8,  8,  8,
     8,  8,  8,  8,  8,  8,  8,  8,  8,  8,
     8,  8,  8,  8,  8,  9,  9,  9,  9,  9,
     9,  9,  9,  9,  9,  9,  9,  9,  9,  9,
     9,  9,  9,  9,  9,  9,  9,  9,  9,  9,
     9,  9,  9,  9,  9, 10, 10, 10, 10, 10,
    10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
    10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
    10, 10, 10, 10, 10, 10, 11, 11, 11, 11,
    11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
    11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
    11, 11, 11, 11, 11, 11, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 12, 
  };


  /**
   * Table that converts day of year to day in leap years.
   */
  protected static final int doy2day_in_leap_year[] = {
     0,  1,  2,  3,  4,  5,  6,  7,  8,  9,
    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
    30, 31,  1,  2,  3,  4,  5,  6,  7,  8,
     9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
    19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
    29,  1,  2,  3,  4,  5,  6,  7,  8,  9,
    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
    30, 31,  1,  2,  3,  4,  5,  6,  7,  8,
     9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
    19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
    29, 30,  1,  2,  3,  4,  5,  6,  7,  8,
     9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
    19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
    29, 30, 31,  1,  2,  3,  4,  5,  6,  7,
     8,  9, 10, 11, 12, 13, 14, 15, 16, 17,
    18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
    28, 29, 30,  1,  2,  3,  4,  5,  6,  7,
     8,  9, 10, 11, 12, 13, 14, 15, 16, 17,
    18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
    28, 29, 30, 31,  1,  2,  3,  4,  5,  6,
     7,  8,  9, 10, 11, 12, 13, 14, 15, 16,
    17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
    27, 28, 29, 30, 31,  1,  2,  3,  4,  5,
     6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
    16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
    26, 27, 28, 29, 30,  1,  2,  3,  4,  5,
     6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
    16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
    26, 27, 28, 29, 30, 31,  1,  2,  3,  4,
     5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
    25, 26, 27, 28, 29, 30,  1,  2,  3,  4,
     5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
    25, 26, 27, 28, 29, 30, 31, 
  };


  /**
   * Table that converts day of year to month in non-leap years.
   */
  protected static final int doy2month_in_ordinary_year[] = {
     0,  1,  1,  1,  1,  1,  1,  1,  1,  1,
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
     1,  1,  2,  2,  2,  2,  2,  2,  2,  2,
     2,  2,  2,  2,  2,  2,  2,  2,  2,  2,
     2,  2,  2,  2,  2,  2,  2,  2,  2,  2,
     3,  3,  3,  3,  3,  3,  3,  3,  3,  3,
     3,  3,  3,  3,  3,  3,  3,  3,  3,  3,
     3,  3,  3,  3,  3,  3,  3,  3,  3,  3,
     3,  4,  4,  4,  4,  4,  4,  4,  4,  4,
     4,  4,  4,  4,  4,  4,  4,  4,  4,  4,
     4,  4,  4,  4,  4,  4,  4,  4,  4,  4,
     4,  5,  5,  5,  5,  5,  5,  5,  5,  5,
     5,  5,  5,  5,  5,  5,  5,  5,  5,  5,
     5,  5,  5,  5,  5,  5,  5,  5,  5,  5,
     5,  5,  6,  6,  6,  6,  6,  6,  6,  6,
     6,  6,  6,  6,  6,  6,  6,  6,  6,  6,
     6,  6,  6,  6,  6,  6,  6,  6,  6,  6,
     6,  6,  7,  7,  7,  7,  7,  7,  7,  7,
     7,  7,  7,  7,  7,  7,  7,  7,  7,  7,
     7,  7,  7,  7,  7,  7,  7,  7,  7,  7,
     7,  7,  7,  8,  8,  8,  8,  8,  8,  8,
     8,  8,  8,  8,  8,  8,  8,  8,  8,  8,
     8,  8,  8,  8,  8,  8,  8,  8,  8,  8,
     8,  8,  8,  8,  9,  9,  9,  9,  9,  9,
     9,  9,  9,  9,  9,  9,  9,  9,  9,  9,
     9,  9,  9,  9,  9,  9,  9,  9,  9,  9,
     9,  9,  9,  9, 10, 10, 10, 10, 10, 10,
    10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
    10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
    10, 10, 10, 10, 10, 11, 11, 11, 11, 11,
    11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
    11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
    11, 11, 11, 11, 11, 12, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 
  };


  /**
   * Table that converts day of year to day in non-leap years.
   */
  protected static final int doy2day_in_ordinary_year[] = {
     0,  1,  2,  3,  4,  5,  6,  7,  8,  9,
    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
    30, 31,  1,  2,  3,  4,  5,  6,  7,  8,
     9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
    19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
     1,  2,  3,  4,  5,  6,  7,  8,  9, 10,
    11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
    31,  1,  2,  3,  4,  5,  6,  7,  8,  9,
    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
    30,  1,  2,  3,  4,  5,  6,  7,  8,  9,
    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
    30, 31,  1,  2,  3,  4,  5,  6,  7,  8,
     9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
    19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
    29, 30,  1,  2,  3,  4,  5,  6,  7,  8,
     9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
    19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
    29, 30, 31,  1,  2,  3,  4,  5,  6,  7,
     8,  9, 10, 11, 12, 13, 14, 15, 16, 17,
    18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
    28, 29, 30, 31,  1,  2,  3,  4,  5,  6,
     7,  8,  9, 10, 11, 12, 13, 14, 15, 16,
    17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
    27, 28, 29, 30,  1,  2,  3,  4,  5,  6,
     7,  8,  9, 10, 11, 12, 13, 14, 15, 16,
    17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
    27, 28, 29, 30, 31,  1,  2,  3,  4,  5,
     6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
    16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
    26, 27, 28, 29, 30,  1,  2,  3,  4,  5,
     6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
    16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
    26, 27, 28, 29, 30, 31,
  };


  /**
   * Table that converts [month][day] to day of year in leap years.
   */
  protected static final int date2doy_in_leap_year[][] = {
  {  0},
  {  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,
         11,  12,  13,  14,  15,  16,  17,  18,  19,  20,
         21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31},
  {  0,  32,  33,  34,  35,  36,  37,  38,  39,  40,  41,
         42,  43,  44,  45,  46,  47,  48,  49,  50,  51,
         52,  53,  54,  55,  56,  57,  58,  59,  60},
  {  0,  61,  62,  63,  64,  65,  66,  67,  68,  69,  70,
         71,  72,  73,  74,  75,  76,  77,  78,  79,  80,
         81,  82,  83,  84,  85,  86,  87,  88,  89,  90,  91},
  {  0,  92,  93,  94,  95,  96,  97,  98,  99, 100, 101,
        102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
        112, 113, 114, 115, 116, 117, 118, 119, 120, 121},
  {  0, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131,
        132, 133, 134, 135, 136, 137, 138, 139, 140, 141,
        142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152},
  {  0, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162,
        163, 164, 165, 166, 167, 168, 169, 170, 171, 172,
        173, 174, 175, 176, 177, 178, 179, 180, 181, 182},
  {  0, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192,
        193, 194, 195, 196, 197, 198, 199, 200, 201, 202,
        203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213},
  {  0, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223,
        224, 225, 226, 227, 228, 229, 230, 231, 232, 233,
        234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244},
  {  0, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254,
        255, 256, 257, 258, 259, 260, 261, 262, 263, 264,
        265, 266, 267, 268, 269, 270, 271, 272, 273, 274},
  {  0, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284,
        285, 286, 287, 288, 289, 290, 291, 292, 293, 294,
        295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305},
  {  0, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315,
        316, 317, 318, 319, 320, 321, 322, 323, 324, 325,
        326, 327, 328, 329, 330, 331, 332, 333, 334, 335},
  {  0, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345,
        346, 347, 348, 349, 350, 351, 352, 353, 354, 355,
        356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366},
  };


  /**
   * Table that converts [month][day] to day of year in non-leap years.
   */
  protected static final int date2doy_in_ordinary_year[][] = {
  {  0},
  {  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,
         11,  12,  13,  14,  15,  16,  17,  18,  19,  20,
         21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31},
  {  0,  32,  33,  34,  35,  36,  37,  38,  39,  40,  41,
         42,  43,  44,  45,  46,  47,  48,  49,  50,  51,
         52,  53,  54,  55,  56,  57,  58,  59},
  {  0,  60,  61,  62,  63,  64,  65,  66,  67,  68,  69,
         70,  71,  72,  73,  74,  75,  76,  77,  78,  79,
         80,  81,  82,  83,  84,  85,  86,  87,  88,  89,  90},
  {  0,  91,  92,  93,  94,  95,  96,  97,  98,  99, 100,
        101, 102, 103, 104, 105, 106, 107, 108, 109, 110,
        111, 112, 113, 114, 115, 116, 117, 118, 119, 120},
  {  0, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130,
        131, 132, 133, 134, 135, 136, 137, 138, 139, 140,
        141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151},
  {  0, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161,
        162, 163, 164, 165, 166, 167, 168, 169, 170, 171,
        172, 173, 174, 175, 176, 177, 178, 179, 180, 181},
  {  0, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191,
        192, 193, 194, 195, 196, 197, 198, 199, 200, 201,
        202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212},
  {  0, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222,
        223, 224, 225, 226, 227, 228, 229, 230, 231, 232,
        233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243},
  {  0, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253,
        254, 255, 256, 257, 258, 259, 260, 261, 262, 263,
        264, 265, 266, 267, 268, 269, 270, 271, 272, 273},
  {  0, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283,
        284, 285, 286, 287, 288, 289, 290, 291, 292, 293,
        294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304},
  {  0, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314,
        315, 316, 317, 318, 319, 320, 321, 322, 323, 324,
        325, 326, 327, 328, 329, 330, 331, 332, 333, 334},
  {  0, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344,
        345, 346, 347, 348, 349, 350, 351, 352, 353, 354,
        355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365},
  };


  /**
   * Julian day number for the start of Julian and Gregorian calendars
   * and their variations.
   */
  public static final int FIRST_JULIAN_DAY = 0;


  public int getFirstJulianDay()
  {
    return FIRST_JULIAN_DAY;
  }
}
