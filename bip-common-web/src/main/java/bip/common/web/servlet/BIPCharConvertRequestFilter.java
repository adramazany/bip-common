package bip.common.web.servlet;

import aip.util.AIPUtil;
import aip.util.NVL;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class BIPCharConvertRequestFilter implements Filter{
	
	protected FilterConfig filterConfig = null;
	
    private String encoding;

	private String srcChars = null;
	private String destChars = null;
	private String urlParamFromEnc = null;
	private String urlParamToEnc = null;

	public void destroy() {
		filterConfig = null;
		System.out.println("BIPRequestFilter.destroy()");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
  	  if(null == request.getCharacterEncoding())
          request.setCharacterEncoding(encoding); 
		
		chain.doFilter(new BIPCharConvertRequestWrapper(request,srcChars,destChars), response);
	}

	public void init(FilterConfig config) throws ServletException {
		System.out.println("BIPRequestFilter.init()");
		filterConfig=config;

        encoding = config.getInitParameter("requestEncoding"); 
        if( encoding==null )encoding=AIPUtil.getSystemCharset(); 
		
		srcChars = filterConfig.getInitParameter("srcchars");
		destChars = filterConfig.getInitParameter("destchars");
		
		System.setProperty("bip.charconvert.srcchars", srcChars);
		System.setProperty("bip.charconvert.destchars", destChars);

	}

}


class BIPCharConvertRequestWrapper extends HttpServletRequestWrapper {

	private String srcChars = null;
	private String destChars = null;
	public BIPCharConvertRequestWrapper(ServletRequest request,
			String srcChars, String destChars) {

		super((HttpServletRequest) request);
		this.srcChars = srcChars;
		this.destChars = destChars;

	}

	public String getParameter(String paramName) {
		String value = super.getParameter(paramName);
		if (!NVL.isEmpty(value) && !NVL.isEmpty(srcChars) && !NVL.isEmpty(destChars)) {
//			value=cnvCharsInURLfromISO2UTF8(paramName,value);
			
			value = cnvCharsSrc2Dest(value);
		}
		return value;
	}

	public String[] getParameterValues(String paramName) {
		String values[] = super.getParameterValues(paramName);
		if(values!=null){
		for (int index = 0; index < values.length; index++) {
			if (!NVL.isEmpty(values[index]) && !NVL.isEmpty(srcChars) && !NVL.isEmpty(destChars)) {
//				values[index] =cnvCharsInURLfromISO2UTF8(paramName,values[index] );
				
				values[index] = AIPUtil.cnvCharsSrc2Dest(values[index],srcChars,destChars);
			}
		}
		}
		return values;
	}

	private String cnvCharsSrc2Dest(String value) {
		StringBuffer sb = new StringBuffer(value);
		for (int i = 0; i < sb.length(); i++) {
			char ch = sb.charAt(i);
			for (int j = 0; j < srcChars.length(); j++) {
				if (ch == srcChars.charAt(j)) {
					// replace founded char with equalient in dest chars
					sb.setCharAt(i, destChars.charAt(j));
				}
			}
		}
		return sb.toString();
	}

	
	
}


class BIPCharConvertResponseWrapper extends HttpServletResponseWrapper {

	public BIPCharConvertResponseWrapper(HttpServletResponse response) {
		super(response);
		
	}
	@Override
	public PrintWriter getWriter() throws IOException {
		return super.getWriter();
	}

	
}
