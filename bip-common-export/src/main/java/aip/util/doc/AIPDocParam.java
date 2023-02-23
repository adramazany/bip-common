package aip.util.doc;

import aip.util.AIPException;
import aip.util.AIPWebUserParam;
import bip.common.util.BIPPrintParam;
import bip.common.util.BIPReportLST;

public class AIPDocParam {
	
	String title;
//	String params;
//	List lst;
//	String columnNames;
//	String options;
	BIPReportLST list=null;
	BIPPrintParam printParam = null;
	
//	int printRange=RANGE_PAGE_CURRENT;
//	int printPageCurrent;
//	int printPageFrom;
//	int printPageTo;
//	String printPageCustom;
//	boolean landscape=false;
//	com.itextpdf.text.Rectangle pageSize=null;
	
	AIPWebUserParam webUserParam=null;
	
	String pdfFontPath ="font";
	String pdfFontName="ARIALUNI.TTF";
	
	
	public BIPPrintParam getPrintParam() {
		return printParam;
	}
	public void setPrintParam(BIPPrintParam printParam) {
		this.printParam = printParam;
	}
	public AIPDocParam(){
	}
//	public AIPDocParam(List lst){
//		this.lst=lst;
//	}
//	public AIPDocParam(List lst,String columnNames) {
//		this.lst=lst;
//		this.columnNames=columnNames;
//	}
//
//	public AIPDocParam(String title,String params,List lst,String columnNames) {
//		this.lst=lst;
//		this.columnNames=columnNames;
//		this.title=title;
//		this.params=params;
//	}
//
//	public void setLst(List lst) {
//		this.lst = lst;
//	}
//	public void setColumnNames(String columnNames) {
//		this.columnNames = columnNames;
//	}
//	public int getPrintPageCurrent() {
//		return printPageCurrent;
//	}
//	public void setPrintPageCurrent(int printPageCurrent) {
//		this.printPageCurrent = printPageCurrent;
//	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//	public String getParams() {
//		return params;
//	}

//	public void setParams(String params) {
//		this.params = params;
//	}
//
//	public List getLst() {
//		return lst;
//	}

//	public String getColumnNames() {
//		return columnNames;
//	}
//	public String getOptions() {
//		return options;
//	}
//	public void setOptions(String options) {
//		this.options = options;
//	}
//	public String[] getSplitedColumns() {
//		String[] cols =null;
//		if(NVL.isEmpty(columnNames)){
//			if(lst!=null && lst.size()>0){
//				ArrayList<Field> flds = AIPUtil.getFields(lst.get(0).getClass());
//				cols=new String[flds.size()];
//				for(int i=0;i<flds.size();i++){
//					cols[i]=flds.get(i).getName();
//				}
//			}
//		}else{
//			cols = AIPUtil.splitSelectedIds(columnNames, ",");
//		}
//		return cols;
//	}
//	public boolean isLandscape() {
//		return landscape;
//	}
//	public void setLandscape(boolean landscape) {
//		this.landscape = landscape;
//	}
//	public com.itextpdf.text.Rectangle getPageSize() {
//		return pageSize;
//	}
//	public void setPageSize(com.itextpdf.text.Rectangle pageSize) {
//		this.pageSize = pageSize;
//	}
//	public void setPageSize(float llx, float lly, float urx, float ury) {
//		this.pageSize = new Rectangle(llx, lly, urx, ury);
//	}
//	public void setPageSize(String pageSizeName) {
//		try{
//			this.pageSize = PageSize.getRectangle(pageSizeName);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//	}
//	public int getPrintRange() {
//		return printRange;
//	}
//	public void setPrintRange(String printRange) {
//		if("PagesAll".equalsIgnoreCase(printRange)){
//			this.printRange=RANGE_PAGE_ALL;
//		}else if("PagesFromTo".equalsIgnoreCase(printRange)){
//			this.printRange=RANGE_PAGE_FROMTO;
//		}else if("PagesCustom".equalsIgnoreCase(printRange)){
//			this.printRange=RANGE_PAGE_CUSTOM;
//		}else{
//			this.printRange=RANGE_PAGE_CURRENT;
//		}
//	}
//	public void setPrintRange(int printRange) {
//		this.printRange = printRange;
//	}
//	public int getPrintPageFrom() {
//		return printPageFrom;
//	}
//	public void setPrintPageFrom(int printPageFrom) {
//		this.printPageFrom = printPageFrom;
//	}
//	public int getPrintPageTo() {
//		return printPageTo;
//	}
//	public void setPrintPageTo(int printPageTo) {
//		this.printPageTo = printPageTo;
//	}
//	public String getPrintPageCustom() {
//		return printPageCustom;
//	}
//	public void setPrintPageCustom(String printPageCustom) {
//		this.printPageCustom = printPageCustom;
//	}
//	
//
//	
//	public static final String[] getPageSizeNames(){
//		return new String[]{
//				"A4"
//				,"LETTER"
//				,"NOTE"
//				,"LEGAL"
//				,"TABLOID"
//				,"EXECUTIVE"
//				,"POSTCARD"
//				,"A0"
//				,"A1"
//				,"A2"
//				,"A3"
//				,"A4"
//				,"A5"
//				,"A6"
//				,"A7"
//				,"A8"
//				,"A9"
//				,"A10"
//				,"B0"
//				,"B1"
//				,"B2"
//				,"B3"
//				,"B4"
//				,"B5"
//				,"B6"
//				,"B7"
//				,"B8"
//				,"B9"
//				,"B10"
//				,"ARCH_E"
//				,"ARCH_D"
//				,"ARCH_C"
//				,"ARCH_B"
//				,"ARCH_A"
//				,"FLSA"
//				,"FLSE"
//				,"HALFLETTER"
//				,"_11X17"
//				,"ID_1"
//				,"ID_2"
//				,"ID_3"
//				,"LEDGER"
//				,"CROWN_QUARTO"
//				,"LARGE_CROWN_QUARTO"
//				,"DEMY_QUARTO"
//				,"ROYAL_QUARTO"
//				,"CROWN_OCTAVO"
//				,"LARGE_CROWN_OCTAVO"
//				,"DEMY_OCTAVO"
//				,"ROYAL_OCTAVO"
//				,"SMALL_PAPERBACK"
//				,"PENGUIN_SMALL_PAPERBACK"
//				,"PENGUIN_LARGE_PAPERBACK"
//		};
//	}
	public BIPReportLST getList() {
		return list;
	}
	public void setList(BIPReportLST list) {
		this.list = list;
	}
	
	public void verify()throws AIPException{
		if(getList()==null){// || printParam.getList().getRows()==null)
			throw new AIPException("لیست گزارش خالیست");
		}else if(getList().getVisibleColumnCount()<=0){
			throw new AIPException("ستونهای گزارش مشخص نشده است");
		}
	}
	public AIPWebUserParam getWebUserParam() {
		return webUserParam;
	}
	public String getWebUserParamUser() {
		if( webUserParam!=null)return webUserParam.getRemoteUser();
		return null;
	}
	public void setWebUserParam(AIPWebUserParam webUserParam) {
		this.webUserParam = webUserParam;
	}
	public String getPdfFontPath() {
		return pdfFontPath;
	}
	public void setPdfFontPath(String pdfFontPath) {
		this.pdfFontPath = pdfFontPath;
	}
	public String getPdfFontName() {
		return pdfFontName;
	}
	public void setPdfFontName(String pdfFontName) {
		this.pdfFontName = pdfFontName;
	}
	
	
}
