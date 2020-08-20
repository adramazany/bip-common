package aip.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

public class AIPOpenOfficeUtilParam {
	public static final int RANGE_PAGE_CURRENT=0;
	public static final int RANGE_PAGE_ALL=1;
	public static final int RANGE_PAGE_FROMTO=2;
	public static final int RANGE_PAGE_CUSTOM=3;
	
	String title;
	String params;
	List lst;
	String columnNames;
	String options;
	
	int printRange=RANGE_PAGE_CURRENT;
	int printPageCurrent;
	int printPageFrom;
	int printPageTo;
	String printPageCustom;
	boolean landscape=false;
	com.itextpdf.text.Rectangle pageSize=null;
	
	
	public AIPOpenOfficeUtilParam(){
	}
	public AIPOpenOfficeUtilParam(List lst){
		this.lst=lst;
	}
	public AIPOpenOfficeUtilParam(List lst,String columnNames) {
		this.lst=lst;
		this.columnNames=columnNames;
	}

	public AIPOpenOfficeUtilParam(String title,String params,List lst,String columnNames) {
		this.lst=lst;
		this.columnNames=columnNames;
		this.title=title;
		this.params=params;
	}

	public void setLst(List lst) {
		this.lst = lst;
	}
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	public int getPrintPageCurrent() {
		return printPageCurrent;
	}
	public void setPrintPageCurrent(int printPageCurrent) {
		this.printPageCurrent = printPageCurrent;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public List getLst() {
		return lst;
	}

	public String getColumnNames() {
		return columnNames;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String[] getSplitedColumns() {
		String[] cols =null;
		if(NVL.isEmpty(columnNames)){
			if(lst!=null && lst.size()>0){
				ArrayList<Field> flds = AIPUtil.getFields(lst.get(0).getClass());
				cols=new String[flds.size()];
				for(int i=0;i<flds.size();i++){
					cols[i]=flds.get(i).getName();
				}
			}
		}else{
			cols = AIPUtil.splitSelectedIds(columnNames, ",");
		}
		return cols;
	}
	public boolean isLandscape() {
		return landscape;
	}
	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}
	public com.itextpdf.text.Rectangle getPageSize() {
		return pageSize;
	}
	public void setPageSize(com.itextpdf.text.Rectangle pageSize) {
		this.pageSize = pageSize;
	}
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
	

}
