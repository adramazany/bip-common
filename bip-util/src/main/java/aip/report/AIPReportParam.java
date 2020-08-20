package aip.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import aip.util.AIPPrintParam;
import aip.util.AIPWebUserParam;
import aip.util.NVL;
import aip.util.log.NIOLogENT;

public class AIPReportParam {
	public final static int PAGE_SIZE=10; 
	
	private int requestPage;
	private int pageSize = PAGE_SIZE; // Must be defined in a general way;
    
//	private String printPageSize;
//    private boolean printPageLandscape=false;
	private AIPPrintParam printParam = new AIPPrintParam();
    
	AIPWebUserParam webUserParam=null;

    private NIOLogENT noLogENT = new NIOLogENT();
    
    
	private String sortcolumn;
	private boolean issortdesc=false;
    
	private String dateType;
	
	
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
	public AIPPrintParam getPrintParam() {
		return printParam;
	}
	public void setPrintParam(AIPPrintParam printParam) {
		this.printParam = printParam;
	}

    
	public void fillParameters(HttpServletRequest request){
		/*
		 * getsession attribute
		 */
		AIPReportParam sessionParam = getSessionParam(request);
		
		/*
		 * parameters
		 */
    	setRequestPage( NVL.getInt(request.getParameter("requestPage"), NVL.getInt(sessionParam.getRequestPage(), 1) ));
		setPageSize( NVL.getInt(request.getParameter("pageSize") , NVL.getInt(sessionParam.getPageSize(), PAGE_SIZE) ));
		setSortcolumn(NVL.getString( request.getParameter("sortcolumn"),sessionParam.getSortcolumn()));
		setIssortdesc(NVL.getBool(request.getParameter("issortdesc"),NVL.getBool(sessionParam.getSortcolumn()) ));
		
		printParam.fillParameters(request,sessionParam.getPrintParam());
		
		webUserParam=new AIPWebUserParam(request);
		
		setDateType(NVL.getStringEmpty(request.getParameter("dateType"),"day"));//day week month
		
		
		/*
		 * set session attribute
		 */
		request.getSession().setAttribute(this.getClass().getName(), this);
	}
	public AIPWebUserParam getWebUserParam() {
		return webUserParam;
	}
	public void setWebUserParam(AIPWebUserParam webUserParam) {
		this.webUserParam = webUserParam;
	}
	
	
    
    
	public NIOLogENT getNoLogENT() {
		return noLogENT;
	}
	public void setNoLogENT(NIOLogENT noLogENT) {
		this.noLogENT = noLogENT;
	}
	
	public String getWebUsername(){
		if(webUserParam==null){
			return null;
		}else{
			return webUserParam.getRemoteUser();
		}
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
	


	public java.lang.String getParamUrl(){
		return "";
	}
	
	public AIPReportParam getSessionParam(HttpServletRequest request){
		AIPReportParam sessionParam = (AIPReportParam) request.getSession().getAttribute(this.getClass().getName());
		if(sessionParam==null)sessionParam=this;
		return sessionParam;
	}

}
