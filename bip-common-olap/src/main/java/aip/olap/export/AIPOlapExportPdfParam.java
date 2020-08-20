package aip.olap.export;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

public class AIPOlapExportPdfParam extends AIPOlapExportParam{
	boolean landscape=true;
	com.itextpdf.text.Rectangle pageSize=PageSize.A4;
	String subject;
	String htmlStyleClass;
	String javascript;

	public AIPOlapExportPdfParam(String title,String[] paramString,AIPOlapExportHeader columnHeader){
		super(title,paramString,columnHeader,null);
	}
	public AIPOlapExportPdfParam(String title,String[] paramString,AIPOlapExportHeader columnHeader,String removeStringFromMembers){
		super(title,paramString,columnHeader,removeStringFromMembers);
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
	
	public boolean isLandscape() {
		return landscape;
	}
	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getHtmlStyleClass() {
		return htmlStyleClass;
	}



	public void setHtmlStyleClass(String htmlStyleClass) {
		this.htmlStyleClass = htmlStyleClass;
	}



	public String getJavascript() {
		return javascript;
	}



	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}

	
}
