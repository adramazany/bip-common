package aip.tag.title;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import aip.tag.AIPBodyTagSupport;
import aip.util.AIPUtil;

public class AIPTitle extends AIPBodyTagSupport {

	String title;
	String align;
	String width;
	
	public AIPTitle() {
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
		try {
			pageContext.getOut().write("");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}
	
	String loadHeaderTitle() {
		StringBuffer sb = null;
//		try {
//			String realPath ="";
//			try {
//				realPath = pageContext.getServletContext().getRealPath("aip/skin/title/aip_title.html");
//			} catch (Exception e) {
//				e.printStackTrace();
//				realPath = pageContext.getServletContext().getRealPath("images/skin/title/aip_title.html");
//			}
//			File f = new File(realPath);
//			FileInputStream fin = new FileInputStream(f);
//			byte buf[] = new byte[fin.available()];
//			fin.read(buf);
//			fin.close();
//			sb = new String(buf);
			sb = loadURLContent("aip/skin/title/aip_title.html");
			if(sb==null){
				sb = loadURLContent("images/skin/title/aip_title.html");
			}
			if(sb==null){
				sb = new StringBuffer();
			}
			
			AIPUtil.replaceAllString(sb, "[TITLE]", getTitle());
			if (align != null) {
				AIPUtil.replaceAllString(sb, "[ALIGN]", align);
			} else {
				AIPUtil.replaceAllString(sb, "[ALIGN]", "center");
			}
			
			if (width != null) {
				AIPUtil.replaceAllString(sb, "[WIDTH]", width);
			} else {
				AIPUtil.replaceAllString(sb, "[WIDTH]", "");
			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return sb.toString();
	}	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

}