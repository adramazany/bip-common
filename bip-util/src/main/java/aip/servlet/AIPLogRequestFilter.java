package aip.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import aip.util.AIPLogRequest;

public class AIPLogRequestFilter implements Filter{
	
	protected FilterConfig filterConfig = null;
	
	public void destroy() {
		filterConfig = null;
		System.out.println("AIPLogRequestFilter.destroy()");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		AIPLogRequest.start2((HttpServletRequest)request);
		
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AIPLogRequest.end2((HttpServletRequest)request);
	}

	public void init(FilterConfig config) throws ServletException {
		System.out.println("AIPLogRequestFilter.init()");
		filterConfig=config;
	}
}
