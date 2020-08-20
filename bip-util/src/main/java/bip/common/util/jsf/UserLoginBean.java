package bip.common.util.jsf;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@RequestScoped
public class UserLoginBean {
	Logger logger = Logger.getLogger(UserLoginBean.class.getName());

	private String username;
	private String password;

	public String login(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
		
		try {
			dispatcher.forward(request, response);
			FacesContext.getCurrentInstance().responseComplete();
		} catch (ServletException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			BIPFacesUtil.addError(e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			BIPFacesUtil.addError(e);
		}
		
		return null;
	}
	
	
	public UserLoginBean() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
