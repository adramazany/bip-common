package aip.olap;

import java.util.ArrayList;
import java.util.List;

import aip.util.NVL;
import fi.joensuu.joyds1.calendar.JalaliCalendar;

public class OlapTarikhUtil {
	public static String cnvTarikhAzTa2TarikhOptimizeSet(String dimTarikhName,String tarikhAzStr,String tarikhTaStr){
		TarikSplited tarikhAz = getTarikSplited(tarikhAzStr);
		TarikSplited tarikhTa = getTarikSplited(tarikhTaStr);
		
		List<String> ranges = new ArrayList<String>();
		boolean finished=false;
		
		/**
		 * 13910105-13931215
		 * =>
		 * {13910105:13910131		1	days-start
		 * ,1391.02:1391.03			2	months-start
		 * ,1391.summer				3	season-start
		 * ,1391.semester2			4	semesters-start
		 * ,1392.year				5	years
		 * ,1393.semester1			6	semesters-end
		 * ,1393.fall				7	seasons-end
		 * ,1393.10:1393.11			8	months-end
		 * ,13931201:13931215}		9	days-end
		 */
		
		 //* {13910105:13910131		1	days-start
		if(tarikhAz.day>1){
			if(tarikhTa.year>tarikhAz.year || tarikhTa.month>tarikhAz.month){
				ranges.add( getDaysToMonthEnd(dimTarikhName, tarikhAz) );
				increaseToNextMonthStart(tarikhAz);
			}else{
				ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
				finished=true;
			}
		}
		//* ,1391.02:1391.03			2	months-start
		if(!finished && tarikhAz.month!=1 && tarikhAz.month!=4 && tarikhAz.month!=7 && tarikhAz.month!=10){//not start of season means :farvardin,tir,mehr,day
			if(tarikhTa.year>tarikhAz.year || isInDiffSeason(tarikhAz,tarikhTa)){
				ranges.add( getMonthsToSeasonEnd(dimTarikhName, tarikhAz) );
				increaseToNextSeasonStart(tarikhAz);
			}else if(tarikhTa.month>tarikhAz.month){
				finished = fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else{
				ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
				finished=true;
			}
		}
		//* ,1391.summer				3	season-start
		if(!finished && tarikhAz.month!=1 && tarikhAz.month!=7){//not start of semester 1,2
			if(tarikhTa.year>tarikhAz.year || isInDiffSemester(tarikhAz,tarikhTa)){
				ranges.add( getSeasonsToSemesterEnd(dimTarikhName, tarikhAz) );
				increaseToNextSemesterStart(tarikhAz);
			}else if(isInDiffSeason(tarikhAz,tarikhTa)){
				finished = fillRanesInSemester(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else if(tarikhTa.month>tarikhAz.month){
				finished = fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else{
				ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
				finished=true;
			}
		}
		//* ,1391.semester2			4	semesters-start
		if(!finished && tarikhAz.month!=1){//not start of year
			if(tarikhTa.year>tarikhAz.year){
				ranges.add( getSemesterToYearEnd(dimTarikhName, tarikhAz) );
				increaseToNextYearStart(tarikhAz);
			}else if(isInDiffSeason(tarikhAz,tarikhTa)){
				finished = fillRanesInSemester(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else if(tarikhTa.month>tarikhAz.month){
				finished = fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else{
				ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
				finished=true;
			}
		}
		//* ,1392.year				5	years
		if(!finished && tarikhAz.month==1 && tarikhAz.day==1){//start of year :tarikhAz.month==1 && tarikhAz.day==1 is for readability not for condition their should be that after previous conditions 
			if(tarikhTa.year>tarikhAz.year){
				ranges.add( getYearsToLastYear(dimTarikhName, tarikhAz,tarikhTa) );
				increaseToLastYearStart(tarikhAz,tarikhTa);
				if( tarikhAz.year>tarikhTa.year){
					finished=true;
				}
			}else if(isInDiffSemester(tarikhAz,tarikhTa)){
				finished = fillRanesInYear(dimTarikhName, ranges, tarikhAz, tarikhTa); 
			}else if(isInDiffSeason(tarikhAz,tarikhTa)){
				finished = fillRanesInSemester(dimTarikhName, ranges, tarikhAz, tarikhTa);
			}else if(tarikhTa.month>tarikhAz.month){
				finished = fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else{
				ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
				finished=true;
			}
		}
		//* ,1393.semester1			6	semesters-end
		if(!finished && isInDiffSemester(tarikhAz,tarikhTa)){ 
			finished = fillRanesInYear(dimTarikhName, ranges, tarikhAz, tarikhTa); 
		}
		//* ,1393.fall				7	seasons-end
		if(!finished && isInDiffSeason(tarikhAz,tarikhTa)){
			ranges.add( getSeason(dimTarikhName, tarikhAz) );
			increaseToNextSeasonStart(tarikhAz);
			finished = fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
		}
		//* ,1393.10:1393.11			8	months-end
		if(!finished && tarikhTa.month>tarikhAz.month){
			finished = fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
		}
		//* ,13931201:13931215}		9	days-end		
		if(!finished){
			ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
			finished=true;
		}
		
		String set =ranges2OlapSet(ranges);
		
		return set;
	}



	private static String ranges2OlapSet(List<String> ranges) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("{");
		for(String range:ranges){
			if(!NVL.isEmpty(range)){
				sb.append(range);
				sb.append(",");
			}
		}
		if(sb.length()>2)sb.delete(sb.length()-1, sb.length());
		sb.append("}");
		
		return sb.toString();
	}



	private static String getSemester(String dimTarikhName, TarikSplited tarikhAz) {
		int monthSemester = tarikhAz.month<7 ? 1 : 2;
		return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, monthSemester, 0, 0, 0);
	}



	/**
	 * if tarikhTa is last day of year year1229 or year1230 then increase to tarikhTa   
	 */
	private static void increaseToLastYearStart(TarikSplited tarikhAz,TarikSplited tarikhTa) {
		int lastYear = tarikhTa.year;
		if(tarikhTa.month==12 && (tarikhTa.day==30 || tarikhTa.day==29 )){// tarikhTa full year
			lastYear++;
		}
		tarikhAz.year=lastYear;
		tarikhAz.month=1;
		tarikhAz.day=1;
		
	}



	/**
	 * if tarikhTa is last day of year year1229 or year1230 then include that year  
	 */
	private static String getYearsToLastYear(String dimTarikhName, TarikSplited tarikhAz,TarikSplited tarikhTa) {
		int lastYear = tarikhTa.year-1;
		if(tarikhTa.month==12 && (tarikhTa.day==30 || tarikhTa.day==29 )){// tarikhTa full year
			lastYear++;
		}
		if(tarikhAz.year<lastYear) {
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, 0, 0, 0)
					+":"
					+getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, lastYear, 0, 0, 0, 0);
		}else{
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, 0, 0, 0);
		}
	}



	private static void increaseToNextYearStart(TarikSplited tarikhAz) {
		tarikhAz.year++;
		tarikhAz.month=1;
		tarikhAz.day=1;
	}



	private static String getSemesterToYearEnd(String dimTarikhName, TarikSplited tarikhAz) {
		if(tarikhAz.month==1){//full year
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, 0, 0, 0);
		}else{//semeaster of year
			int monthSemester = tarikhAz.month<7 ? 1 : 2;
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, monthSemester, 0, 0, 0);
		}
	}



	/**
	 * if tarikhTa is last day of season then add season not month seperatly 
	 */
	private static boolean fillRanesInSeason(String dimTarikhName, List ranges,TarikSplited tarikhAz, TarikSplited tarikhTa) {
		if(((tarikhTa.day==31 && (tarikhTa.month==3 || tarikhTa.month==6))
				 || (tarikhTa.day==30 && (tarikhTa.month==9 || tarikhTa.month==12))
				 || (tarikhTa.day==29 && tarikhTa.month==12)
			) && (tarikhAz.month==1 && tarikhAz.day==1)
				){//full season
			ranges.add( getSeason(dimTarikhName, tarikhAz) );
		}else{
			if(tarikhTa.month>tarikhAz.month){
				ranges.add( getMonth(dimTarikhName, tarikhAz) );
				increaseToNextMonthStart(tarikhAz);
				if(tarikhTa.month>tarikhAz.month){
					ranges.add( getMonth(dimTarikhName, tarikhAz) );
					increaseToNextMonthStart(tarikhAz);
				}
			}
			ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
		}

		return true;
	}
	private static boolean fillRanesInSemester(String dimTarikhName, List ranges,TarikSplited tarikhAz, TarikSplited tarikhTa) {
		if(((tarikhTa.day==31 && (tarikhTa.month==6))
				 || (tarikhTa.month==12 && (tarikhTa.day==29 || tarikhTa.day==30))
			) && (tarikhAz.month==1 || tarikhAz.month==7) && (tarikhAz.day==1)
				){//full semester
			ranges.add( getSemester(dimTarikhName, tarikhAz) );
		}else{
			if(tarikhTa.month>tarikhAz.month){
				ranges.add( getSeason(dimTarikhName, tarikhAz) );
				increaseToNextSeasonStart(tarikhAz);
				fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else{
				ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
			}
		}

		return true;
	}
	private static boolean fillRanesInYear(String dimTarikhName, List ranges,TarikSplited tarikhAz, TarikSplited tarikhTa) {
		if((tarikhTa.month==12 && (tarikhTa.day==29 || tarikhTa.day==30)
			) && tarikhAz.month==1 && tarikhAz.day==1){//full year
			ranges.add( getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, 0, 0, 0) );
		}else{
			ranges.add( getSemester(dimTarikhName, tarikhAz) );
			increaseToNextSemesterStart(tarikhAz);
			if(isInDiffSeason(tarikhAz, tarikhTa)){
				fillRanesInSemester(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else if(tarikhTa.month>tarikhAz.month){
				fillRanesInSeason(dimTarikhName, ranges,tarikhAz,tarikhTa);
			}else{
				ranges.add( getStartToEndInMonth(dimTarikhName, tarikhAz,tarikhTa) );
			}
		}
		return true;
	}



	private static String getSeason(String dimTarikhName, TarikSplited tarikhAz) {
		int monthSeason = getMonthSeason(tarikhAz.month);
		return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, monthSeason, 0, 0);
	}



	private static void increaseToNextSemesterStart(TarikSplited tarikhAz) {
		int monthSemester = tarikhAz.month<7 ? 1 : 2;
		monthSemester++;
		if(monthSemester>2){
			monthSemester=1;
			tarikhAz.year++;
		}
		tarikhAz.month=monthSemester*6-5;
		tarikhAz.day=1;
	}



	private static String getSeasonsToSemesterEnd(String dimTarikhName, TarikSplited tarikhAz) {
		if(tarikhAz.month==1 || tarikhAz.month==7 ){//full semester
			int monthSemester = tarikhAz.month<7 ? 1 : 2;
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, monthSemester, 0, 0,0);
		}else{//season of semeaster
			int monthSeason = getMonthSeason(tarikhAz.month);
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, monthSeason, 0, 0);
		}
	}



	private static boolean isInDiffSemester(TarikSplited tarikhAz, TarikSplited tarikhTa) {
		if((tarikhAz.month<7 && tarikhTa.month<7)
		|| (tarikhAz.month>=7 && tarikhTa.month>=7) 
		){
			return false;
		}else{
			return true;
		}
	}



	private static String getMonth(String dimTarikhName, TarikSplited tarikhAz) {
		return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, tarikhAz.month,0);
	}



	private static boolean isInDiffSeason(TarikSplited tarikhAz,TarikSplited tarikhTa) {
		return getMonthSeason(tarikhAz.month)!=getMonthSeason(tarikhTa.month);
	}



	private static void increaseToNextSeasonStart(TarikSplited tarikhAz) {
		int monthSeason = getMonthSeason(tarikhAz.month);
		monthSeason++;
		if(monthSeason>4){
			monthSeason=1;
			tarikhAz.year++;
		}
		tarikhAz.month=monthSeason*3-2;
		tarikhAz.day=1;
	}



	private static String getMonthsToSeasonEnd(String dimTarikhName, TarikSplited tarikhAz) {
		int monthSeason = getMonthSeason(tarikhAz.month);
		if(tarikhAz.month==1 || tarikhAz.month==4 || tarikhAz.month==7 || tarikhAz.month==10){//full season
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, monthSeason, 0,0);
		}else if(tarikhAz.month==3 || tarikhAz.month==6 || tarikhAz.month==9 || tarikhAz.month==12){//one month
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, 0, 0, tarikhAz.month,0);
		}else{//months of season
			int seasonLastMonth = monthSeason*3;
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, tarikhAz.month,0)
					+":"
					+getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, seasonLastMonth,0);
		}
	}



	private static int getMonthSeason(int month) {
		int season=0;
		switch (month) {
		case 1:case 2: case 3:
			season=1;
			break;
		case 4:case 5: case 6:
			season=2;
			break;
		case 7:case 8: case 9:
			season=3;
			break;
		case 10:case 11: case 12:
			season=4;
			break;
		}
		return season;
	}



	/**
	 * start must be start of month year-month-1
	 * if startToEnd is fixed a month specially esfand in 2 (year1229,year1230) case it should return month not from:to 
	 */
	private static String getStartToEndInMonth(String dimTarikhName, TarikSplited tarikhAz,TarikSplited tarikhTa) {
		if(tarikhTa.year>=tarikhAz.year && tarikhTa.month>=tarikhAz.month && tarikhTa.day>=tarikhAz.day){
			if(((tarikhTa.day==31 && tarikhTa.month<7)
					 || (tarikhTa.day==30 && tarikhTa.month>=7)
					 || (tarikhTa.day==29 && tarikhTa.month==12)
				) && tarikhAz.day==1
					){//full month
				return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, tarikhAz.month,0);
			}else{//part of month
				return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, tarikhAz.month,tarikhAz.day)
						+":"
						+getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhTa.year, tarikhTa.month, tarikhTa.day);
			}
		}
		return "";
	}



	private static void increaseToNextMonthStart(TarikSplited tarikhAz) {
		tarikhAz.month++;
		if(tarikhAz.month>12){
			tarikhAz.month=1;
			tarikhAz.year++;
		}
		tarikhAz.day=1;
	}

	private static String getDaysToMonthEnd(String dimTarikhName, TarikSplited tarikhAz) {
		if(tarikhAz.day==1){
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, tarikhAz.month,0);
		}else{
			int lastDayOfThisMonth =31;
			if(tarikhAz.month==12){
				lastDayOfThisMonth = (new JalaliCalendar()).isLeapYear(tarikhAz.year) ? 30 : 29;
			}else if(tarikhAz.month>=7 && tarikhAz.month<=11){
				lastDayOfThisMonth=30;
			}
			return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, tarikhAz.month,tarikhAz.day)
					+":"
					+getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, tarikhAz.year, tarikhAz.month,lastDayOfThisMonth);
		}
	}


	private static String getTarikh2HierSaalNimsaalFaslMahRooz(String dimTarikhName, int year,int month,int day) {
		int semesterCalcFromMonth=0;
		int seasonCalcFromMonth=0;
		return getTarikh2HierSaalNimsaalFaslMahRooz(dimTarikhName, year, semesterCalcFromMonth, seasonCalcFromMonth, month, day);
	}
	/**
	 * @param dimTarikhName
	 * @param year
	 * @param semester
	 * @param season
	 * @param month
	 * @param day
	 * @return
	 */
	private static String getTarikh2HierSaalNimsaalFaslMahRooz(String dimTarikhName, int year,int semester,int season,int month,int day) {
//		return DimTarikhName+".["+param.getTarikhsaal()+"].[1].[1].[1].[1]:"+DimTarikhName+".["+param.getTarikhsaal()+"].[2].[3].[9].[30]";
		StringBuffer sb = new StringBuffer();
		sb.append(dimTarikhName);
			
		sb.append(".[");
		sb.append(year);
		sb.append("]");
		if(month>0){
			switch (month) {
			case 1:case 2: case 3:
				sb.append(".[1].[1]");
				break;
			case 4:case 5: case 6:
				sb.append(".[1].[2]");
				break;
			case 7:case 8: case 9:
				sb.append(".[2].[3]");
				break;
			case 10:case 11: case 12:
				sb.append(".[2].[4]");
				break;
			}
			sb.append(".[");
			sb.append(month);
			sb.append("]");
			if(day>0 && day<32){
				sb.append(".[");
				sb.append(day);
				sb.append("]");
			}
		}else if(season>0 && season<5){
			switch (season) {
			case 1:case 2:
				sb.append(".[1]");
				break;
			case 3:case 4:
				sb.append(".[2]");
				break;
			}
			sb.append(".[");
			sb.append(season);
			sb.append("]");
		}else{
			if(semester>0 && semester<3){
				sb.append(".[");
				sb.append(semester);
				sb.append("]");
			}
		}

		return sb.toString();
	}

	private static TarikSplited getTarikSplited(String tarikh){
		TarikSplited result = new TarikSplited();
		String[] tarikhs = tarikh.split("/");
		result.year = NVL.getInt(tarikhs[0]);
		result.month = NVL.getInt(tarikhs[1]);
		result.day = NVL.getInt(tarikhs[2]);
		//int[] result = {year,month,day};
		return result;
	}


/********************************************************************************************/
	public static void main(String[] args) {
		long d1=System.currentTimeMillis();
		System.out.println("OlapTarikhUtil.main():starting......");
		String[][] examples={
				{"1388/01/01","1392/12/29"}
				,{"1392/01/01","1392/09/30"}
				,{"1392/02/01","1392/03/31"}
				,{"1391/02/31","1391/02/31"}
				,{"1392/01/01","1392/01/31"}
				,{"1392/01/01","1392/01/15"}
				,{"1392/01/01","1392/02/31"}
				,{"1392/01/01","1392/02/30"}
				,{"1392/01/01","1392/03/31"}
				,{"1392/01/01","1392/03/30"}
				,{"1392/01/01","1392/05/31"}
				,{"1392/01/01","1392/05/30"}
				,{"1392/01/01","1392/06/31"}
				,{"1392/01/01","1392/06/30"}
				,{"1392/01/01","1392/07/29"}
				,{"1392/01/01","1392/07/30"}
				,{"1392/01/01","1392/08/30"}
				,{"1392/01/01","1392/08/29"}
				,{"1392/01/01","1392/09/29"}
				,{"1392/01/01","1392/10/30"}
				,{"1392/01/01","1392/10/29"}
				,{"1391/01/01","1393/08/29"}
				,{"1391/01/05","1393/12/15"}
				,{"1391/09/01","1392/01/31"}
				,{"1391/09/01","1392/01/30"}
				,{"1391/09/01","1392/02/31"}
				,{"1391/09/01","1392/02/30"}
				,{"1391/09/01","1392/03/31"}
				,{"1391/09/01","1392/03/30"}
				,{"1391/09/01","1392/04/31"}
				,{"1391/09/01","1392/04/30"}
				,{"1391/09/01","1392/05/31"}
				,{"1391/09/01","1392/05/30"}
				,{"1391/09/01","1392/06/31"}
				,{"1391/09/01","1392/06/30"}
				,{"1391/09/01","1392/07/30"}
				,{"1391/09/01","1392/07/29"}
				,{"1391/09/01","1392/08/30"}
				,{"1391/09/01","1392/08/29"}
				,{"1391/09/01","1392/09/30"}
				,{"1391/09/01","1392/09/29"}
				,{"1391/09/05","1392/09/30"}
				,{"1391/09/05","1392/09/29"}
				};
		
		String[] asserts={
		"{DimTarikhSabttavalod.[1388]:DimTarikhSabttavalod.[1392]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1].[2],DimTarikhSabttavalod.[1392].[1].[1].[3]}"
		,"{DimTarikhSabttavalod.[1391].[1].[1].[2].[31]:DimTarikhSabttavalod.[1391].[1].[1].[2].[31]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1].[1]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1].[1].[1]:DimTarikhSabttavalod.[1392].[1].[1].[1].[15]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1].[1],DimTarikhSabttavalod.[1392].[1].[1].[2]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1].[1],DimTarikhSabttavalod.[1392].[1].[1].[2].[1]:DimTarikhSabttavalod.[1392].[1].[1].[2].[30]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1].[1],DimTarikhSabttavalod.[1392].[1].[1].[2],DimTarikhSabttavalod.[1392].[1].[1].[3].[1]:DimTarikhSabttavalod.[1392].[1].[1].[3].[30]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4],DimTarikhSabttavalod.[1392].[1].[2].[5]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4],DimTarikhSabttavalod.[1392].[1].[2].[5].[1]:DimTarikhSabttavalod.[1392].[1].[2].[5].[30]}"
		,"{DimTarikhSabttavalod.[1392].[1]}"
		,"{DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4],DimTarikhSabttavalod.[1392].[1].[2].[5],DimTarikhSabttavalod.[1392].[1].[2].[6].[1]:DimTarikhSabttavalod.[1392].[1].[2].[6].[30]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7].[1]:DimTarikhSabttavalod.[1392].[2].[3].[7].[29]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7],DimTarikhSabttavalod.[1392].[2].[3].[8]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7],DimTarikhSabttavalod.[1392].[2].[3].[8].[1]:DimTarikhSabttavalod.[1392].[2].[3].[8].[29]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7],DimTarikhSabttavalod.[1392].[2].[3].[8],DimTarikhSabttavalod.[1392].[2].[3].[9].[1]:DimTarikhSabttavalod.[1392].[2].[3].[9].[29]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3],DimTarikhSabttavalod.[1392].[2].[4].[10]}"
		,"{DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3],DimTarikhSabttavalod.[1392].[2].[4].[10].[1]:DimTarikhSabttavalod.[1392].[2].[4].[10].[29]}"
		,"{DimTarikhSabttavalod.[1391]:DimTarikhSabttavalod.[1392],DimTarikhSabttavalod.[1393].[1],DimTarikhSabttavalod.[1393].[2].[3].[7],DimTarikhSabttavalod.[1393].[2].[3].[8].[1]:DimTarikhSabttavalod.[1393].[2].[3].[8].[29]}"
		,"{DimTarikhSabttavalod.[1391].[1].[1].[1].[5]:DimTarikhSabttavalod.[1391].[1].[1].[1].[31],DimTarikhSabttavalod.[1391].[1].[1].[2]:DimTarikhSabttavalod.[1391].[1].[1].[3],DimTarikhSabttavalod.[1391].[1].[2],DimTarikhSabttavalod.[1391].[2],DimTarikhSabttavalod.[1392],DimTarikhSabttavalod.[1393].[1],DimTarikhSabttavalod.[1393].[2].[3],DimTarikhSabttavalod.[1393].[2].[4].[10],DimTarikhSabttavalod.[1393].[2].[4].[11],DimTarikhSabttavalod.[1393].[2].[4].[12].[1]:DimTarikhSabttavalod.[1393].[2].[4].[12].[15]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1].[1]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1].[1].[1]:DimTarikhSabttavalod.[1392].[1].[1].[1].[30]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1].[1],DimTarikhSabttavalod.[1392].[1].[1].[2]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1].[1],DimTarikhSabttavalod.[1392].[1].[1].[2].[1]:DimTarikhSabttavalod.[1392].[1].[1].[2].[30]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1].[1],DimTarikhSabttavalod.[1392].[1].[1].[2],DimTarikhSabttavalod.[1392].[1].[1].[3].[1]:DimTarikhSabttavalod.[1392].[1].[1].[3].[30]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4].[1]:DimTarikhSabttavalod.[1392].[1].[2].[4].[30]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4],DimTarikhSabttavalod.[1392].[1].[2].[5]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4],DimTarikhSabttavalod.[1392].[1].[2].[5].[1]:DimTarikhSabttavalod.[1392].[1].[2].[5].[30]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1].[1],DimTarikhSabttavalod.[1392].[1].[2].[4],DimTarikhSabttavalod.[1392].[1].[2].[5],DimTarikhSabttavalod.[1392].[1].[2].[6].[1]:DimTarikhSabttavalod.[1392].[1].[2].[6].[30]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7].[1]:DimTarikhSabttavalod.[1392].[2].[3].[7].[29]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7],DimTarikhSabttavalod.[1392].[2].[3].[8]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7],DimTarikhSabttavalod.[1392].[2].[3].[8].[1]:DimTarikhSabttavalod.[1392].[2].[3].[8].[29]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7],DimTarikhSabttavalod.[1392].[2].[3].[8],DimTarikhSabttavalod.[1392].[2].[3].[9].[1]:DimTarikhSabttavalod.[1392].[2].[3].[9].[29]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9].[5]:DimTarikhSabttavalod.[1391].[2].[3].[9].[30],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3]}"
		,"{DimTarikhSabttavalod.[1391].[2].[3].[9].[5]:DimTarikhSabttavalod.[1391].[2].[3].[9].[30],DimTarikhSabttavalod.[1391].[2].[4],DimTarikhSabttavalod.[1392].[1],DimTarikhSabttavalod.[1392].[2].[3].[7],DimTarikhSabttavalod.[1392].[2].[3].[8],DimTarikhSabttavalod.[1392].[2].[3].[9].[1]:DimTarikhSabttavalod.[1392].[2].[3].[9].[29]}"
		};
		
		
		String dimTarikhName="DimTarikhSabttavalod";
		for (int i = 0; i < examples.length; i++) {
			String tarikhAzStr=examples[i][0];
			String tarikhTaStr=examples[i][1];
			String set=OlapTarikhUtil.cnvTarikhAzTa2TarikhOptimizeSet(dimTarikhName, tarikhAzStr, tarikhTaStr);
			System.out.println(""+i+")"+tarikhAzStr+":"+tarikhTaStr+"=>"+set);
			//vm option should be -ea to assert work
			assert set.equalsIgnoreCase(asserts[i]):"failed!!! "+i+")"+tarikhAzStr+":"+tarikhTaStr+"=>"+set+" ,correct result=="+asserts[i];
			
		}
		long d2=System.currentTimeMillis();
		System.out.println("OlapTarikhUtil.main():end at: "+(d2-d1)+" ms");
	}
	
}
