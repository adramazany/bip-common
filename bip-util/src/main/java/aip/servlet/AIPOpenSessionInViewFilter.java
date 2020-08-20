package aip.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session;

import aip.orm.HibernateSessionFactory;

/**
 * Servlet Filter implementation class AIPOpenSessionInViewFilter
 */
/*@WebFilter("/AIPOpenSessionInViewFilter")*/
public class AIPOpenSessionInViewFilter implements Filter {

	static Logger logger = Logger.getLogger(AIPOpenSessionInViewFilter.class.getName()); 
    /**
     * Default constructor. 
     */
    public AIPOpenSessionInViewFilter() {
        //System.out.println("AIPOpenSessionInViewFilter.AIPOpenSessionInViewFilter()");
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		System.out.println("AIPOpenSessionInViewFilter.destroy()");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//System.out.println("AIPOpenSessionInViewFilter.doFilter():starting.......");
		try {
			Session session = HibernateSessionFactory.getSession();
			if(session!=null && (session.getTransaction()==null || !session.getTransaction().isActive())){
				//System.out.println("AIPOpenSessionInViewFilter.doFilter():session.beginTransaction");
				session.beginTransaction();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.severe(e.getMessage());
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		try {
			Session session = HibernateSessionFactory.getSession();
			if(session!=null && (session.getTransaction()!=null && session.getTransaction().isActive())){
				try {
					session.getTransaction().commit();
					//System.out.println("AIPOpenSessionInViewFilter.doFilter():tx.commit");
				} catch (Exception e) {
					//e.printStackTrace();
					logger.severe(e.getMessage());
					if(session.getTransaction()!=null && session.getTransaction().isActive()){
						session.getTransaction().rollback();
						//System.err.println("AIPOpenSessionInViewFilter.doFilter():tx.rollback");
						logger.severe("tx.rollback");
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.severe(e.getMessage());
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("AIPOpenSessionInViewFilter.init()");
	}

}
