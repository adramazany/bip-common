package bip.common.obiee.servlet;

/**
 * Created by ramezani on 11/28/2019.
 */
//import com.view.util.JSFUtils;
/*
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.io.UnsupportedEncodingException;

import java.net.CookieHandler;
//import java.net.CookieManager;
//import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.xml.namespace.QName;

import oracle.bi.web.soap.AuthResult;
import oracle.bi.web.soap.SAWLocale;
import oracle.bi.web.soap.SAWSessionParameters;
import oracle.bi.web.soap.SAWSessionService;
import oracle.bi.web.soap.SAWSessionServiceSoap;
*/
/**
 * This class is used as bridge to retrive data from OBIEE server using secured connecton.
 */
public class BridgeServlet {/*extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String relatedtUrl = request.getParameter("RedirectURL");
        String cookiename=getCookie(request, "ORA_BIPS_NQID");
        StringBuffer url = new StringBuffer(relatedtUrl);
        Map parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext(); ) {
            String parameterName = (String)iterator.next();
            String[] parameterValues = (String[])parameterMap.get(parameterName);
            if (parameterValues != null && parameterValues.length > 0) {
                if (parameterName.equals("RedirectURL")) {
                    continue;
                }
                for (int i = 0; i < parameterValues.length; i++) {
                    url.append("&");
                    url.append(parameterName);
                    url.append("=");
                    String value = "";
                    try {
                        value = URLEncoder.encode(parameterValues[i], "UTF-8");
                        //value = URLDecoder.decode(parameterValues[i], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    url.append(value);
                }
            }
        }

        try {
            URL urlconn = new URL(url.toString());
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            URLConnection httpConnection = (URLConnection)urlconn.openConnection();
            ((HttpURLConnection)httpConnection).setRequestMethod("GET");
            httpConnection.setRequestProperty("Cookie", "ORA_BIPS_NQID="+cookiename+"; path=/analytics;HttpOnly");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true); //Only if you expect to read a response…
            httpConnection.setUseCaches(false); //Highly recommended…
            httpConnection.setRequestProperty("Content-Type", "binary/data");
            httpConnection.setRequestProperty("User-Agent", request.getHeader("USER-AGENT"));
            InputStream in = (InputStream)httpConnection.getInputStream();
            response.setContentType("text/plain; charset=utf-8");
            ServletOutputStream out = response.getOutputStream();
            byte buffer1[] = new byte[1024 * 128];
            int k = 0;
            while ((k = in.read(buffer1)) != -1) {
                out.write(buffer1, 0, k);
            }
            in.close();
            out.close();
            ((HttpURLConnection)httpConnection).disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String relatedtUrl = request.getParameter("RedirectURL");
        String cookiename=getCookie(request, "ORA_BIPS_NQID");

        //System.out.println("cookie setting "+cookiename);
        StringBuffer url = new StringBuffer(relatedtUrl);
        Map parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext(); ) {
            String parameterName = (String)iterator.next();
            String[] parameterValues = (String[])parameterMap.get(parameterName);
            if (parameterValues != null && parameterValues.length > 0) {
                if (parameterName.equals("RedirectURL")) {
                    continue;
                }

                for (int i = 0; i < parameterValues.length; i++) {
                    url.append("&");
                    url.append(parameterName);
                    url.append("=");
                    String value = "";
                    try {
                        value = URLEncoder.encode(parameterValues[i], "UTF-8");
                        //value = URLDecoder.decode(parameterValues[i], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    url.append(value);
                }
            }
        }



        try {
            URL urlconn = new URL(url.toString());
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            URLConnection httpConnection = (URLConnection)urlconn.openConnection();
            ((HttpURLConnection)httpConnection).setRequestMethod("POST");
            httpConnection.setRequestProperty("Cookie", "ORA_BIPS_NQID="+cookiename+";path=/analytics;HttpOnly");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true); //Only if you expect to read a response…
            httpConnection.setUseCaches(false); //Highly recommended…
            httpConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            httpConnection.setRequestProperty("User-Agent", request.getHeader("USER-AGENT"));
            InputStream in = (InputStream)httpConnection.getInputStream();
            response.setContentType("text/plain; charset=utf-8");
            ServletOutputStream out = response.getOutputStream();
            byte buffer1[] = new byte[1024 * 128];
            int k = 0;
            while ((k = in.read(buffer1)) != -1) {
                out.write(buffer1, 0, k);
            }
            in.close();
            out.close();
            ((HttpURLConnection)httpConnection).disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getCookie(HttpServletRequest request,String  cookieName){
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
*/
}
