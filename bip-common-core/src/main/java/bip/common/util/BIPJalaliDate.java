package bip.common.util;

import bip.common.util.calendar.JalaliCalendar;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BIPJalaliDate {
	int year;
	int month;
	int day;

	private JalaliCalendar jc = new JalaliCalendar();

	public BIPJalaliDate(){
		year = jc.getYear();
		month = jc.getMonth();
		day = jc.getDay();
	}
	public BIPJalaliDate(String date){
		this( NVL.getInt(date) );
	}
	public BIPJalaliDate(int date){
		if(date>0 && date<9999){
			year = date;
			month = 6;
			day = 31;
		}else if(date>0 && date<999999){
			year = date/100;
			month = date % 100;
			day = 15;
		}else if(date>0){
			year = date/10000;
			int monthday = date % 10000;
			month = monthday / 100;
			day = monthday % 100;
		}
		
		correct_year1230_notexist_to_year1229();
	}
	public static BIPJalaliDate parseFullDate(String fullDate){
		fullDate = fullDate.replaceAll("/", "");
		return new BIPJalaliDate(fullDate);
	}
	
	public static float diffYear(BIPJalaliDate first,BIPJalaliDate next){
//		int year = next.year - first.year;
//		int month = next.month - first.month;
//		int day = next.day - first.day;
//		if(month<0){
//			year--;
//		}else if(month==0 && day<0) {
//			year--;
//		}
//		return year;
		int diffDay = diffDay(first, next);
		int year = diffDay / 365;
		int mandehDay = diffDay % 365;
		float diffYear = (float)year+((float)mandehDay*(float)0.001);
		
		DecimalFormat df = new DecimalFormat("##.###");
		df.setRoundingMode(RoundingMode.DOWN);
		diffYear=(float) NVL.getDbl( df.format(diffYear) );
		
		return diffYear;
	}

	public static float diffMonth(BIPJalaliDate first,BIPJalaliDate next){
		JalaliCalendar jc = new JalaliCalendar();
		 
		int year = next.year - first.year;
		float month = next.month - first.month;
		int day = next.day - first.day;
		if(month<0){
			if(day<0){
				month--;
				int monthlen =getLengthOfMonth(next.year, next.month-1);
				day+=monthlen;
			}
			year--;
			month+=12;
		}else if(month==0 && day<0) {
			year--;
			month+=12;
		}else if(day<0) {
			month--;
			int monthlen =getLengthOfMonth(next.year, next.month-1);
			day+=monthlen;
		}
		month = year*12+month + ((float)day/100);
		
		return month;
	}

	public static int diffDay(BIPJalaliDate first,BIPJalaliDate next){
		JalaliCalendar jcFirst = new JalaliCalendar(first.year, first.month, first.day);
		JalaliCalendar jcNext = new JalaliCalendar(next.year, next.month, next.day);
		
		return jcNext.compareTo(jcFirst);
	}

	private static int getLengthOfMonth(int year , int month){
		int result;
		if(month<1){
			year--;
			month+=12;
		}
		if(month==12){
			JalaliCalendar jc = new JalaliCalendar();
			if(jc.isLeapYear(year)){
				result=30;
			}else{
				result=29;
			}
		}else if(month>6){
			result=30;
		}else{
			result=31;
		}
		return result;
	}
	
	public void addDays(int days){
		JalaliCalendar jc = new JalaliCalendar(year, month, day);
		
		jc.addDays(days);
		
		year=jc.getYear();
		month=jc.getMonth();
		day=jc.getDay();
	}
	public int getDateInt(){
		return year*10000+month*100+day;
	}

	public static String getFullDate(String date) {
		if(!NVL.isEmpty(date)){
			String ar[] = date.split("/");
			int year = NVL.getInt(ar[0]);
			int month = 1;
			if(ar.length>1)month=NVL.getInt(ar[1]);
			int day=1;
			if(ar.length>2)day=NVL.getInt(ar[2]);
			
			date = getFullDate(year,month,day);
		}
		return date;
		
	}
	public static String getFullDate(int year , int month, int day) {
        return year + "/" + ( (month < 10)  ? "0" + month : month) + "/" + ((day < 10) ? "0" + day : day);
    }
	
	public String getFullDate(){
		return getFullDate(year , month, day);
	}
	
	public int getDayOfWeek(){
		jc.set(year, month, day);
		return jc.getDayOfWeek();
	}
	

	
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}

	private void correct_year1230_notexist_to_year1229(){
		if(month==12 && day==30){
			try {
				JalaliCalendar jc = new JalaliCalendar(year, month, day);
			} catch (Exception e) {
				day=29;
			}
		}
		
	}

	public static void main(String[] args) {
		System.out.println("FacttavalodETL.main():start..........");
		
//		BIPJalaliDate d1 = new BIPJalaliDate(13620101);
//		BIPJalaliDate d2 = new BIPJalaliDate(13870707);
//		
//		System.out.println("FacttavalodETL.main():diffYear"+BIPJalaliDate.diffYear(d1, d2));
//		System.out.println("FacttavalodETL.main():diffMonth"+BIPJalaliDate.diffMonth(d1, d2));
//		System.out.println("FacttavalodETL.main():diffDay"+BIPJalaliDate.diffDay(d1, d2));
		
		BIPJalaliDate d1 = new BIPJalaliDate(12991230);
		System.out.println("BIPJalaliDate.main()d1="+d1.getFullDate());
		BIPJalaliDate d2 = new BIPJalaliDate();
		System.out.println("BIPJalaliDate.main():diffDay=" + BIPJalaliDate.diffDay(d1, d2) );
		

		System.out.println("FacttavalodETL.main():end.");
	}

	
}