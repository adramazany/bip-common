package bip.common.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XSSFilter implements Filter {
    Logger LOG = LoggerFactory.getLogger(XSSFilter.class);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(req instanceof HttpServletRequest){
            HttpServletRequest httpReq = (HttpServletRequest)req;
            LOG.debug("XSSFilter.doFilter: contentType="+httpReq.getContentType()+" , contentLength= "+httpReq.getContentLength()+" , contentPath="+httpReq.getContextPath()+", pathInfo="+httpReq.getPathInfo()+" , queryString="+httpReq.getQueryString()+" , requestURI="+httpReq.getRequestURI()+" , requestURL="+httpReq.getRequestURL().toString());
            XSSRequestWrapper xssReq = new XSSRequestWrapper((HttpServletRequest) req);
            chain.doFilter(xssReq, res);
        }else{
            chain.doFilter(req, res);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {}

    public void destroy() {}
}
