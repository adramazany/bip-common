package aip.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import aip.util.AIPUtil;
import aip.util.NVL;
import aip.util.UTF8;

public class AIPCharConvertRequestFilter implements Filter{
	
	protected FilterConfig filterConfig = null;
	
    private String encoding;

	private String srcChars = null;
	private String destChars = null;
	private String urlParamFromEnc = null;
	private String urlParamToEnc = null;

	public void destroy() {
		filterConfig = null;
		System.out.println("AIPRequestFilter.destroy()");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//System.out.println("AIPRequestFilter.doFilter()");
		//chain.doFilter(new AIPCharConvertRequestWrapper(request,srcChars,destChars), response);
//		chain.doFilter(new AIPCharConvertRequestWrapper(request,srcChars,destChars,urlParamFromEnc,urlParamToEnc), response);
		
  	  if(null == request.getCharacterEncoding())
          request.setCharacterEncoding(encoding); 
		
		chain.doFilter(new AIPCharConvertRequestWrapper(request,srcChars,destChars), response);
	}

	public void init(FilterConfig config) throws ServletException {
		System.out.println("AIPRequestFilter.init()");
		filterConfig=config;

        encoding = config.getInitParameter("requestEncoding"); 
        if( encoding==null )encoding=AIPUtil.getSystemCharset(); 
		
		srcChars = filterConfig.getInitParameter("srcchars");
		destChars = filterConfig.getInitParameter("destchars");
		
		System.setProperty("aip.charconvert.srcchars", srcChars);
		System.setProperty("aip.charconvert.destchars", destChars);

	}

}



class AIPCharConvertRequestWrapper extends HttpServletRequestWrapper {

	private String srcChars = null;
	private String destChars = null;
//	private String urlParamFromEnc = null;
//	private String urlParamToEnc = null;

//	public AIPCharConvertRequestWrapper(ServletRequest request,
//			String srcChars, String destChars,String urlParamFromEnc,String urlParamToEnc) {
//		this(request, srcChars, destChars);
//		this.urlParamFromEnc = urlParamFromEnc;
//		this.urlParamToEnc = urlParamToEnc;
//	}
//
	public AIPCharConvertRequestWrapper(ServletRequest request,
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
	
//	private String cnvCharsInURLfromISO2UTF8(String paramName, String value) {
//		if(!NVL.isEmpty(urlParamFromEnc) && !NVL.isEmpty(urlParamToEnc)){
//			if(this.getQueryString()!=null && this.getQueryString().indexOf(paramName+"=")>-1){
//				//value = cnvEnc2Enc(value,"ISO-8859-1","UTF-8");
//				value = cnvEnc2Enc(value,urlParamFromEnc,urlParamToEnc);
//			}
//		}
//		return value;
//	}
//	public String cnvEnc2Enc(String text,String fromENC,String toEnc) {
//		String encoded = null;
//		if (null != text && !text.equals("")) {
//		    try {
//		          byte[] stringBytesISO = text.getBytes(fromENC); 
//		          encoded = new String(stringBytesISO, toEnc);
//		      }catch(UnsupportedEncodingException e) {
//		          throw new RuntimeException(e); 
//		      } 
//		} else {
//			encoded = text; 
//		} 
//		return encoded; 
//	}


	
	
}




class AIPCharConvertResponseWrapper extends HttpServletResponseWrapper {

	public AIPCharConvertResponseWrapper(HttpServletResponse response) {
		super(response);
		
	}
	@Override
	public PrintWriter getWriter() throws IOException {
		return super.getWriter();
	}

	
}
