package aip.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class AIPSessionCounter implements HttpSessionListener {
	private static final Logger LOGGER = Logger.getLogger(AIPSessionCounter.class.getName());
	
	private List<String> sessions = new ArrayList<String>();

	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		sessions.add( session.getId() );
		session.setAttribute("sessioncounter", this);
		
		LOGGER.log(Level.INFO, "sessionCreated", session.getId());
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		sessions.remove(session.getId());
		session.setAttribute("sessioncounter", this);

		LOGGER.log(Level.INFO, "sessionDestroyed", session.getId());
	}
	
	public int getActiveSessionNumber(){
		return sessions.size();
	}

}
