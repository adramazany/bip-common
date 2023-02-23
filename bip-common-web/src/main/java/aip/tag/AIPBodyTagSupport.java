package aip.tag;

import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AIPBodyTagSupport extends BodyTagSupport {
	public static int relaoTimeInterval = 60000;//1000*60 = 1m
	long lastCheckTime=0;
	long lastSkinFileReaded=0;
	StringBuffer buffer=null;

	public StringBuffer loadURLContent(String resourcepath) {
		if (buffer==null || (lastCheckTime + relaoTimeInterval) < System.currentTimeMillis()) {
			lastCheckTime = System.currentTimeMillis();
			try {
				//String contextPath = pageContext.getServletContext().getContextPath()+"/";
				String contextPath = "/";
				String urlString = contextPath+resourcepath;
				//System.out.println("AIPSkin.loadSkinURLContent():url="+urlString);
				URL url= pageContext.getServletContext().getResource(urlString);
				if(url==null){
					throw new IllegalAccessError("AIPBodyTagSupport.loadURLContent(): resourcepath not found!:"+urlString);
				}
				URLConnection urlConnection = url.openConnection();

				InputStream fin = urlConnection.getInputStream();
				byte buf[] = new byte[fin.available()];
				fin.read(buf);
				fin.close();
				buffer = new StringBuffer( new String(buf));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}
	public String getStartTag(String resourcepath,String placeHolder){
		/*
		 * split start and end
		 */
		StringBuffer content = loadURLContent(resourcepath); 
		int pos = content.indexOf(placeHolder);
		return content.substring(0,pos);
	}
	public String getEndTag(String resourcepath,String placeHolder){
		StringBuffer content = loadURLContent(resourcepath); 
		int pos = content.indexOf(placeHolder);
		return content.substring(pos+placeHolder.length());
	}	
}
