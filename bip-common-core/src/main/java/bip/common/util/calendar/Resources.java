/* fi/joensuu/joyds1/calendar/Resources.java
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

import java.util.*;

/**
 * Localized resources.
 */
public class Resources {
  /**
   * Retrieve a message from the ResourceBundle.
   */
  public static final String getString (String key)
  {
    return r.getString (key);
  }


  /**
   * Retrieve a message from the ResourceBundle.
   */
  public static final String getString (String key, String parameter)
  {
    Object[] args = {new String (parameter)};
    String pattern = getString (key);
    return java.text.MessageFormat.format (pattern, args);
  }


  /**
   * Retrieve a message from the ResourceBundle.
   */
  public static final String getString (String key, String p1, String p2, String p3)
  {
    Object[] args = {new String (p1), new String (p2), new String (p3)};
    String pattern = getString (key);
    return java.text.MessageFormat.format (pattern, args);
  }


  /**
   * Retrieve a message from the ResourceBundle.
   */
  public static final String getString (String key, char parameter)
  {
    char[] p = new char[1];
    p[0] = parameter;
    Object[] args = {new String(p)};
    String pattern = getString (key);
    return java.text.MessageFormat.format (pattern, args);
  }


  /**
   * Get string array corresponding to the key.
   *
   * @param key       The key for the array.
   * @param delimiter The string that separates the components of the array.
   * @param locale    Locale to be used.
   */
  public static final String[] getDelimitedStringArray (String key, String delimiter, Locale locale)
  {
    return getArray (getBundle(locale).getString(key), delimiter);
  }


  /**
   * Get string array corresponding to the key using the first character of
   * the localized string as a delimiter.
   *
   * @param key  The key for the array.
   * @throws IllegalArgumentException if delimiter is a letter or a digit.
   */
  public static final String[] getStringArray (String key)
  {
    return getArray (getString (key));
  }


  /**
   * Convert string to array of tokens using the first
   * character of the string as a token delimiter.
   * The delimiter can not be a letter or a digit.
   */
  private static final String[] getArray (String string)
  {
    String delimiter = string.substring (0, 1);

    if (Character.isLetterOrDigit (delimiter.charAt(0))) {
      throw new IllegalArgumentException ("LetterOrDigit");
    }

    return getArray (string.substring(1), delimiter);
  }


  /**
   * Convert string to array of tokens using delimiter as a token separator.
   */
  private static final String[] getArray (String string, String delimiter)
  {
    StringTokenizer st = new StringTokenizer (string, delimiter);
    String[] s = new String[st.countTokens()];

    for (int i = 0; i < s.length; i++) {
      s[i] = st.nextToken();
    }

    return s;
  }


  /**
   * Get ResourceBundle for the locale.
   */
  public static final ResourceBundle getBundle (Locale locale)
  {
/*
    ResourceBundle b = PropertyResourceBundle.getBundle (RESOURCE_BUNDLE_NAME, locale);
//System.out.println ("Res " + b.getLocale());
    return b;
*/
    return PropertyResourceBundle.getBundle (RESOURCE_BUNDLE_NAME, locale);
  }


  /**
   * Get ResourceBundle for default locale.
   */
  public static final ResourceBundle getBundle()
  {
    return r;
  }


  /**
   * Get date formats for calendar and locale.
   */
  public static final String[] getDateFormats (Calendar calendar, Locale locale)
  {
    ResourceBundle r = getBundle (locale);

    try {
      String className = calendar.getClass().getName();
      String formatKey = className.substring (className.lastIndexOf('.')+1) + "DateFormat";
      String formatString = r.getString (formatKey);

      if (formatString.length() == 0) {
        return getArray (r.getString ("DefaultDateFormat"));
      }
      else {
        return getArray (formatString);
      }
    }
    catch (MissingResourceException m)
    {
      return getArray (r.getString ("DefaultDateFormat"));
    }
  }


  private static final String RESOURCE_BUNDLE_NAME = "fi/joensuu/joyds1/calendar/properties/ResourceBundle";


  // Localized strings are kept in a separate file.
  //
  private static ResourceBundle r =
    PropertyResourceBundle.getBundle (RESOURCE_BUNDLE_NAME, Locale.getDefault());
}
