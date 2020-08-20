package aip.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.util.RequestUtils;

import aip.tag.AIPBodyTagSupport;

public class AIPMessage extends AIPBodyTagSupport {

	String errorMessage = "";
	String successMessage = "";
	String errorName = "";

	String successName = "";

	
	static String contextStart;
	static String contextEnd;

	public AIPMessage() {
		super();
	}
	
	public int doStartTag() throws JspException {
		int res = super.doStartTag();
		try {
			pageContext.getOut().write(loadHeaderTitle());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	public int doAfterBody() throws JspException {
		try {
			BodyContent bodyContent = getBodyContent();
			JspWriter out = bodyContent.getEnclosingWriter();
			bodyContent.writeOut(out);
			bodyContent.clearBody();
		} catch (Exception ex) {
			throw new JspException("error in AIPPagination: " + ex);
		}
		return super.doAfterBody();
	}

	public int doEndTag() throws JspException {
		int i = super.doEndTag();
//		try {
//			pageContext.getOut().write("");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		return i;
	}
	
	String loadHeaderTitle() {
		String sb = "";

		String error = getErrorMessage();
		String success = getSuccessMessage();
		
		if(!NVL.isEmpty(getErrorName())){
			//error+=NVL.getString(pageContext.getAttribute(getErrorName()));
			error+=NVL.getString(AIPUtil.getAttributeAnyScope(pageContext, getErrorName()));
		}
		if(!NVL.isEmpty(getSuccessName())){
			//success+=NVL.getString(pageContext.getAttribute(getSuccessName()));
			success+=NVL.getString(AIPUtil.getAttributeAnyScope(pageContext, getSuccessName()));
		}
		
		
		if(!NVL.isEmpty(error) || !NVL.isEmpty(success) ){
			if(NVL.isEmpty(contextStart) || NVL.isEmpty(contextEnd) ){
				loadContext(pageContext);
			}
			
			sb = contextStart;
			if(!NVL.isEmpty(error)){
				sb+="<label id='errorDescription' style='color:red;'>"+error+"</label>";
			}
			if(!NVL.isEmpty(success)){
				if(!NVL.isEmpty(error)){
					sb+="<br>";
				}
				sb+="<label id='successDescription' style='color:green;'>"+success+"</label>";
			}
			sb+=contextEnd;
		}
		return sb;
	}	
	
	
	
	private void loadContext(PageContext pageContext) {
//		try {
//			String realPath="";
//			try {
//				realPath = pageContext.getServletContext().getRealPath("aip/skin/message/aipskin_message.html");
//			} catch (Exception e) {
//				realPath = pageContext.getServletContext().getRealPath("images/skin/message/aipskin_message.html");
//			}
////			String realPath = "/home/aip/programs/MyEclipse_6.5_workspace/AIPNIOPDCSell/WebRoot/images/skin/message/aipskin_message.html";
//			File f = new File(realPath);
//			FileInputStream fin = new FileInputStream(f);
//			byte buf[] = new byte[fin.available()];
//			fin.read(buf);
//			fin.close();
//			String sb = new String(buf);
			
			StringBuffer sb = loadURLContent("aip/skin/message/aipskin_message.html");
			if(sb==null){
				sb = loadURLContent("images/skin/message/aipskin_message.html");
			}
			if(sb==null){
				sb = new StringBuffer();
			}
			
			
			contextStart = sb.substring(0,sb.indexOf("<td class='rr_YELLOW_center' id='messageContainer'>")+"<td class='rr_YELLOW_center' id='messageContainer'>".length());
			contextEnd = sb.substring(sb.indexOf("<td class='rr_YELLOW_center' id='messageContainer'>")+"<td class='rr_YELLOW_center' id='messageContainer'>".length());
		
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public String getSuccessName() {
		return successName;
	}

	public void setSuccessName(String successName) {
		this.successName = successName;
	}
	
	public static void main(String[] args) {
		AIPMessage message = new AIPMessage();
		
		message.loadHeaderTitle();
	}

}