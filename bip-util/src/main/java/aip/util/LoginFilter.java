package aip.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoginFilter implements Filter {
	
	protected FilterConfig filterConfig = null;

	public void destroy() {
		filterConfig = null;
		//System.out.println("LoginFilter.destroy()");
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		//System.out.println("LoginFilter.doFilter():before");
		chain.doFilter(req, resp);
		System.out.println("LoginFilter.doFilter():after");
	}

	public void init(FilterConfig config) throws ServletException {
		filterConfig=config;
		System.out.println("LoginFilter.init()");

	}

}
