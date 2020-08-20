package bip.common.util;

import bip.common.util.calendar.JalaliCalendar;

import java.util.Calendar;

/**
 * Created by ramezani on 6/15/2020.
 */
public class BIPDongle {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis()/(365*24*60*60*1000));
        Calendar c = Calendar.getInstance();
        System.out.println((double)c.get(Calendar.YEAR)/2025);
        JalaliCalendar jc = new JalaliCalendar();
        System.out.println((double)jc.getYear()/1404);
        System.out.println("Math.atan(2025)="+Math.atan(2025)*1000+500);
        System.out.println(""+1234/1000);
        System.out.println(""+1234/100%10);
        System.out.println(""+1234/10%10);
        System.out.println(""+1234/1%10);

        //1,592,231,160,509
    }
}
