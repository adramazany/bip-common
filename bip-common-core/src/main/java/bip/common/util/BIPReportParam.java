package bip.common.util;

//import bip.common.util.AIPPrintParam;
//import aip.util.AIPWebUserParam;
import aip.util.NVL;
import aip.util.log.NIOLogENT;

//import javax.servlet.http.HttpServletRequest;

public class BIPReportParam {
	public final static int PAGE_SIZE=10; 
	
	private int requestPage;
	private int pageSize = PAGE_SIZE; // Must be defined in a general way;
    
//	private String printPageSize;
//    private boolean printPageLandscape=false;
	private BIPPrintParam printParam = new BIPPrintParam();
    
	//AIPWebUserParam webUserParam=null;

    private NIOLogENT noLogENT = new NIOLogENT();
    
    
	private String sortcolumn;
	private boolean issortdesc=false;
    
	private String dateType;

	String webUsername;
	
	
    public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public int getRequestPage() {
		return (requestPage<=0?1:requestPage);
	}
	public void setRequestPage(int requestPage) {
		this.requestPage = requestPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
//	public String getPrintPageSize() {
//		return printPageSize;
//	}
//	public void setPrintPageSize(String printPageSize) {
//		this.printPageSize = printPageSize;
//	}
//	public boolean isPrintPageLandscape() {
//		return printPageLandscape;
//	}
//	public void setPrintPageLandscape(boolean printPageLandscape) {
//		this.printPageLandscape = printPageLandscape;
//	}
	public BIPPrintParam getPrintParam() {
		return printParam;
	}
	public void setPrintParam(BIPPrintParam printParam) {
		this.printParam = printParam;
	}

    
	public void fillParameters(BIPRequestWrapper request){
		/*
		 * getsession attribute
		 */
		BIPReportParam sessionParam = getSessionParam(request);
		
		/*
		 * parameters
		 */
    	setRequestPage( aip.util.NVL.getInt(request.getParameter("requestPage"), aip.util.NVL.getInt(sessionParam.getRequestPage(), 1) ));
		setPageSize( aip.util.NVL.getInt(request.getParameter("pageSize") , aip.util.NVL.getInt(sessionParam.getPageSize(), PAGE_SIZE) ));
		setSortcolumn(aip.util.NVL.getString( request.getParameter("sortcolumn"),sessionParam.getSortcolumn()));
		setIssortdesc(aip.util.NVL.getBool(request.getParameter("issortdesc"), aip.util.NVL.getBool(sessionParam.getSortcolumn()) ));
		
		printParam.fillParameters(request,sessionParam.getPrintParam());
		
		webUsername=request.getRemoteUser();
		
		setDateType(NVL.getStringEmpty(request.getParameter("dateType"),"day"));//day week month
		
		
		/*
		 * set session attribute
		 */
		request.setSessionAttribute(this.getClass().getName(), this);
	}

    
	public NIOLogENT getNoLogENT() {
		return noLogENT;
	}
	public void setNoLogENT(NIOLogENT noLogENT) {
		this.noLogENT = noLogENT;
	}
	
	public String getSortcolumn() {
		return sortcolumn;
	}
	public void setSortcolumn(String sortcolumn) {
		this.sortcolumn = sortcolumn;
	}
	public boolean isIssortdesc() {
		return issortdesc;
	}
	public void setIssortdesc(boolean issortdesc) {
		this.issortdesc = issortdesc;
	}
	


	public String getParamUrl(){
		return "";
	}
	
	public BIPReportParam getSessionParam(BIPRequestWrapper request){
		BIPReportParam sessionParam = (BIPReportParam) request.getSessionAttribute(this.getClass().getName());
		if(sessionParam==null)sessionParam=this;
		return sessionParam;
	}

	public String getWebUsername(){
		return webUsername;
	}
	public void setWebUsername(String webUsername) {
		this.webUsername = webUsername;
	}
}
