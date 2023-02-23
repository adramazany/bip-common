package bip.common.util;

import aip.util.NVL;
/*
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

import javax.servlet.http.HttpServletRequest;
*/

public class BIPPrintParam {
	public static final int RANGE_PAGE_CURRENT=0;
	public static final int RANGE_PAGE_ALL=1;
	public static final int RANGE_PAGE_FROMTO=2;
	public static final int RANGE_PAGE_CUSTOM=3;

	private String printPageSize;
    private boolean printPageLandscape=false;
    
	int printRange=RANGE_PAGE_CURRENT;
	int printPageCurrent;
	int printPageFrom;
	int printPageTo;
	String printPageCustom;
	boolean landscape=false;
	String pageSize=null;

	public int getPrintPageCurrent() {
		return printPageCurrent;
	}
	public void setPrintPageCurrent(int printPageCurrent) {
		this.printPageCurrent = printPageCurrent;
	}
	public boolean isLandscape() {
		return landscape;
	}
	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
/*
	public void setPageSize(float llx, float lly, float urx, float ury) {
		this.pageSize = new Rectangle(llx, lly, urx, ury);
	}
	public void setPageSize(String pageSizeName) {
		try{
			this.pageSize = PageSize.getRectangle(pageSizeName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
*/
	public int getPrintRange() {
		return printRange;
	}
	public void setPrintRange(String printRange) {
		if("PagesAll".equalsIgnoreCase(printRange)){
			this.printRange=RANGE_PAGE_ALL;
		}else if("PagesFromTo".equalsIgnoreCase(printRange)){
			this.printRange=RANGE_PAGE_FROMTO;
		}else if("PagesCustom".equalsIgnoreCase(printRange)){
			this.printRange=RANGE_PAGE_CUSTOM;
		}else{
			this.printRange=RANGE_PAGE_CURRENT;
		}
	}
	public void setPrintRange(int printRange) {
		this.printRange = printRange;
	}
	public int getPrintPageFrom() {
		return printPageFrom;
	}
	public void setPrintPageFrom(int printPageFrom) {
		this.printPageFrom = printPageFrom;
	}
	public int getPrintPageTo() {
		return printPageTo;
	}
	public void setPrintPageTo(int printPageTo) {
		this.printPageTo = printPageTo;
	}
	public String getPrintPageCustom() {
		return printPageCustom;
	}
	public void setPrintPageCustom(String printPageCustom) {
		this.printPageCustom = printPageCustom;
	}
	
	public String getPrintPageSize() {
		return printPageSize;
	}
	public void setPrintPageSize(String printPageSize) {
		this.printPageSize = printPageSize;
	}
	public boolean isPrintPageLandscape() {
		return printPageLandscape;
	}
	public void setPrintPageLandscape(boolean printPageLandscape) {
		this.printPageLandscape = printPageLandscape;
	}
	
	public String getRequestPages(){
		String pages="";
		if(printRange==RANGE_PAGE_CURRENT){
			pages=""+printPageCurrent;
		}else if(printRange==RANGE_PAGE_ALL){
			pages="0";
		}else if(printRange==RANGE_PAGE_FROMTO){
			pages= ""+printPageFrom+"-"+printPageTo;
		}else if(printRange==RANGE_PAGE_CUSTOM){
			pages=printPageCustom;
		}
		return pages;
	}

	public long[][] getRequestStartsAndEnds(int pageSize,long totalRows){
		String requestPages = getRequestPages();
		if("0".equals(requestPages))return new long[][]{{0,totalRows}};//means all
		String[] pages = requestPages.split(",");
		long[][] startsAndEnds= new long[pages.length][2];
		for(int i=0;i<pages.length;i++){
			String[] pair = pages[i].split("-");
			long page = aip.util.NVL.getLng(pair[0])-1;
			long startRow = (page<0?0:page)*pageSize+1;
			startsAndEnds[i][0]= startRow;
			if(pair.length>1){
				startsAndEnds[i][1]= startRow-1+ aip.util.NVL.getLng(pair[1])*pageSize;
			}else{
				startsAndEnds[i][1]= startRow-1+pageSize;
			}
		}
		return startsAndEnds;
	}
	
	public long[][] getRequestStartsAndEnds(int pageSize){
		String requestPages = getRequestPages();
		if("0".equals(requestPages))return new long[][]{{0,0}};//means all
		String[] pages = requestPages.split(",");
		long[][] startsAndEnds= new long[pages.length][2];
		for(int i=0;i<pages.length;i++){
			String[] pair = pages[i].split("-");
			long page = aip.util.NVL.getLng(pair[0])-1;
			long startRow = (page<0?0:page)*pageSize+1;
			startsAndEnds[i][0]= startRow;
			if(pair.length>1){
				startsAndEnds[i][1]= startRow-1+ aip.util.NVL.getLng(pair[1])*pageSize;
			}else{
				startsAndEnds[i][1]= startRow-1+pageSize;
			}
		}
		return startsAndEnds;
	}

	
	public static final String[] getPageSizeNames(){
		return new String[]{
				"A4"
				,"LETTER"
				,"NOTE"
				,"LEGAL"
				,"TABLOID"
				,"EXECUTIVE"
				,"POSTCARD"
				,"A0"
				,"A1"
				,"A2"
				,"A3"
				,"A4"
				,"A5"
				,"A6"
				,"A7"
				,"A8"
				,"A9"
				,"A10"
				,"B0"
				,"B1"
				,"B2"
				,"B3"
				,"B4"
				,"B5"
				,"B6"
				,"B7"
				,"B8"
				,"B9"
				,"B10"
				,"ARCH_E"
				,"ARCH_D"
				,"ARCH_C"
				,"ARCH_B"
				,"ARCH_A"
				,"FLSA"
				,"FLSE"
				,"HALFLETTER"
				,"_11X17"
				,"ID_1"
				,"ID_2"
				,"ID_3"
				,"LEDGER"
				,"CROWN_QUARTO"
				,"LARGE_CROWN_QUARTO"
				,"DEMY_QUARTO"
				,"ROYAL_QUARTO"
				,"CROWN_OCTAVO"
				,"LARGE_CROWN_OCTAVO"
				,"DEMY_OCTAVO"
				,"ROYAL_OCTAVO"
				,"SMALL_PAPERBACK"
				,"PENGUIN_SMALL_PAPERBACK"
				,"PENGUIN_LARGE_PAPERBACK"
		};
	}
	public void fillParameters(BIPRequestWrapper request, BIPPrintParam sessionParam ) {
		setPrintRange(aip.util.NVL.getString(request.getParameter("printRange")
							, aip.util.NVL.getString(sessionParam.getPrintRange())
				));
		setPrintPageCurrent(aip.util.NVL.getInt(request.getParameter("printRange")
							, aip.util.NVL.getInt(sessionParam.getPrintPageCurrent())
				));
		setPrintPageFrom(aip.util.NVL.getInt(request.getParameter("printPageFrom")
							, aip.util.NVL.getInt(sessionParam.getPrintPageFrom())
				));
		setPrintPageTo(aip.util.NVL.getInt(request.getParameter("printPageTo")
							, aip.util.NVL.getInt(sessionParam.getPrintPageTo())
				));
		setPrintPageCustom(aip.util.NVL.getString(request.getParameter("printPageCustom")
							, aip.util.NVL.getString(sessionParam.getPrintPageCustom())
				));

		setPrintPageSize( aip.util.NVL.getString(request.getParameter("printPageSize")
							, aip.util.NVL.getString(sessionParam.getPrintPageSize())
				));
		setLandscape( aip.util.NVL.getBool(request.getParameter("printPageLandscape")
							, aip.util.NVL.getBool(sessionParam.isLandscape())
				));
	}
	
	
	public long[][] getRequestStartsAndSizes(int pageSize,long totalRows){
		String requestPages = getRequestPages();
		if("0".equals(requestPages))return new long[][]{{0,totalRows}};//means all
		String[] pages = requestPages.split(",");
		long[][] startsAndEnds= new long[pages.length][2];
		for(int i=0;i<pages.length;i++){
			String[] pair = pages[i].split("-");
			long page = aip.util.NVL.getLng(pair[0])-1;
			long startRow = (page<0?0:page)*pageSize;
			startsAndEnds[i][0]= startRow;
			if(pair.length>1){
				startsAndEnds[i][1]= NVL.getLng(pair[1])*pageSize;
			}else{
				startsAndEnds[i][1]= pageSize;
			}
		}
		return startsAndEnds;
	}


	
}
