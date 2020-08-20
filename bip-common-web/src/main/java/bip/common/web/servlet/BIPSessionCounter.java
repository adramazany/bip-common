package bip.common.web.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BIPSessionCounter implements HttpSessionListener {
	private static final Logger LOGGER = Logger.getLogger(BIPSessionCounter.class.getName());
	
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
