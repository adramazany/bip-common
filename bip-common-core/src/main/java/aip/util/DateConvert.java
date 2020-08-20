package aip.util;

//import fi.joensuu.joyds1.calendar.JalaliCalendar;

import bip.common.util.calendar.JalaliCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateConvert
{
  public static String getTodayJalali()
  {
    return gregorian2jalali(new GregorianCalendar());
  }
  
  public static String jalali2gregorian(String jalaliDate)
  {
/*
    String[] str = jalaliDate.split("/");
    int year = Integer.parseInt(str[0]);
    int month = Integer.parseInt(str[1]);
    int day = Integer.parseInt(str[2]);
    int addedDays = 0;
    int gregorianYear = countAddedDays(month, day) >= 287 + isLeapYear(year) ? 622 + year : 621 + year;
    Calendar calendar = new GregorianCalendar(gregorianYear, 0, 1);
    if (countAddedDays(month, day) >= 287 + isLeapYear(year)) {
      addedDays = countAddedDays(month, day) - (287 + isLeapYear(year));
    } else if (countAddedDays(month, day) < 287 + isLeapYear(year)) {
      addedDays = countAddedDays(month, day) + 78;
    }
    calendar.add(6, addedDays);
    return completeDate(gregorianYear, calendar.get(2) + 1, calendar.get(5));
*/
/*
    String[] str = jalaliDate.split("/");
    int year = Integer.parseInt(str[0]);
    int month = Integer.parseInt(str[1]);
    int day = Integer.parseInt(str[2]);
    JalaliCalendar jc = new JalaliCalendar(year,month,day);
*/
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    return sdf.format(jalali2Date(jalaliDate));
//    return sdf.format(new Date());
  }

  public static JalaliCalendar parseJalaliCalendar(String jalaliDate)
  {
    if (NVL.isEmpty(jalaliDate)) {
      return null;
    }
    String[] ar = jalaliDate.split("/");
    return new JalaliCalendar(NVL.getInt(ar[0]), NVL.getInt(ar[1]), NVL.getInt(ar[2]));
  }
  
  public static Date jalali2Date(String jalaliDate)
  {
    JalaliCalendar jc = parseJalaliCalendar(jalaliDate);
    return jc.toJavaUtilGregorianCalendar().getTime();
  }
  
  public int countAddedDays(int month, int day)
  {
    if (month <= 6) {
      return (month - 1) * 31 + day;
    }
    if ((month > 6) && (month <= 11)) {
      return 186 + (month - 7) * 30 + day;
    }
    if (month == 12) {
      return 336 + day;
    }
    return 0;
  }
  
  public static String gregorian2jalali(String gregorianDay)
  {
    GregorianCalendar calendar = (GregorianCalendar)getDay(gregorianDay);
    






    return gregorian2jalali(calendar);
  }
  
  public static String gregorian2jalali(GregorianCalendar gregorianDay)
  {
    JalaliCalendar jc = new JalaliCalendar(gregorianDay);
    






    return completeDate(jc.getYear(), jc.getMonth(), jc.getDay());
  }
  
  private static String creatDate(int year, int dayOfYear)
  {
    if (dayOfYear <= 186) {
      return dayOfYear % 31 == 0 ? completeDate(year, dayOfYear / 31, 31) : completeDate(year, dayOfYear / 31 + 1, dayOfYear % 31);
    }
    if ((dayOfYear > 186) && (dayOfYear <= 365))
    {
      dayOfYear -= 186;
      return dayOfYear % 30 == 0 ? completeDate(year, 6 + dayOfYear / 30, 30) : completeDate(year, 6 + dayOfYear / 30 + 1, dayOfYear % 30);
    }
    if (dayOfYear == 366) {
      return completeDate(year, 12, 30);
    }
    return "0";
  }
  
  public static Calendar getDay(String gregorianDay)
  {
    String[] str = gregorianDay.split("/");
    int year = Integer.parseInt(str[0]);
    int month = Integer.parseInt(str[1]);
    int day = Integer.parseInt(str[2]);
    Calendar calendar = new GregorianCalendar(year, month - 1, day);
    return calendar;
  }
  
  public static String completeDateTime(String dateTime)
  {
    if (!NVL.isEmpty(dateTime))
    {
      int pos = dateTime.indexOf(" ");
      if (pos > 0) {
        dateTime = completeDate(dateTime.substring(0, pos)) + dateTime.substring(pos);
      } else {
        dateTime = completeDate(dateTime);
      }
    }
    return dateTime;
  }
  
  public static String completeDate(String date)
  {
    if (!NVL.isEmpty(date))
    {
      String[] ar = date.split("/");
      int year = NVL.getInt(ar[0]);
      int month = 1;
      if (ar.length > 1) {
        month = NVL.getInt(ar[1]);
      }
      int day = 1;
      if (ar.length > 2) {
        day = NVL.getInt(ar[2]);
      }
      date = completeDate(year, month, day);
    }
    return date;
  }
  
  public static String completeDate(int year, int month, int day)
  {
    return year + "/" + (month < 10 ? "0" + month : Integer.valueOf(month)) + "/" + (day < 10 ? "0" + day : Integer.valueOf(day));
  }
  
  public static int isLeapYear(int jalaliYear)
  {
    if (((jalaliYear % 4 == 0) && (jalaliYear / 10 % 2 == 0)) || (((jalaliYear % 10 == 2) || (jalaliYear % 10 == 6)) && (jalaliYear / 10 % 2 != 0))) {
      return 1;
    }
    return 0;
  }
  
  public static int[] split(String date)
  {
    int[] parts = new int[3];
    String[] ar = date.split("/");
    if (ar.length >= 3)
    {
      parts[0] = NVL.getInt(ar[0]);
      parts[1] = NVL.getInt(ar[1]);
      parts[2] = NVL.getInt(ar[2]);
    }
    return parts;
  }
  
  public static String[] extractFromToDate2Days(String fromDate, String toDate)
  {
    return extractFromToDate2Days(fromDate, toDate, 0, 0);
  }
  
  public static String[] extractFromToDate2Days(String fromDate, String toDate, int fromAddRemoveDays, int toAddRemoveDays)
  {
    ArrayList<String> days = new ArrayList();
    JalaliCalendar jcFrom = new JalaliCalendar();
    JalaliCalendar jcTo = new JalaliCalendar();
    
    int[] parts = split(fromDate);
    jcFrom.set(parts[0], parts[1], parts[2]);
    jcFrom.addDays(fromAddRemoveDays);
    
    parts = split(toDate);
    jcTo.set(parts[0], parts[1], parts[2]);
    jcTo.addDays(toAddRemoveDays);
    while (jcFrom.compareTo(jcTo) <= 0)
    {
      days.add(completeDate(jcFrom.getYear(), jcFrom.getMonth(), jcFrom.getDay()));
      jcFrom.addDays(1);
    }
    return (String[])days.toArray(new String[] { "" });
  }
  
  public static String getTime()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    return sdf.format(new Date());
  }
  
  public static String addDay(String date, int days)
  {
    JalaliCalendar jc = new JalaliCalendar();
    int[] parts = split(date);
    jc.set(parts[0], parts[1], parts[2]);
    jc.addDays(days);
    return completeDate(jc.getYear(), jc.getMonth(), jc.getDay());
  }
  
  public static String gregorian2jalali(Date date)
  {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    return gregorian2jalali(gc);
  }
  
  public static void main(String[] args)
  {
    String st = "DateConvert.main()";
//    System.out.println("DateConvert.main():" + getTodayJalali());
//    System.out.println("DateConvert.main():" + gregorian2jalali("2014/1/27"));
    System.out.println("DateConvert.jalali2Date:" + jalali2Date("1396/12/29"));
    System.out.println("DateConvert.jalali2gregorian:" + jalali2gregorian("1396/12/29"));

  }
}
