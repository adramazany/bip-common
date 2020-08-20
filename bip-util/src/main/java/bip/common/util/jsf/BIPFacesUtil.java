package bip.common.util.jsf;

import java.util.Map;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import aip.util.AIPUtil;

public class BIPFacesUtil {
	private static final Logger logger = Logger.getLogger(BIPFacesUtil.class.getName());
	
	public static Map<String, Object> getSessionMap(){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return externalContext.getSessionMap();
		
	}
	public static void putSession(String key,Object obj){
		getSessionMap().put(key, obj);
	}
	public static Object getSession(String key){
		return getSessionMap().get(key);
	}
	
	public static String getRemoteUser(){
		return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	}

	public static void addError(Exception e){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL
				,null,AIPUtil.getExceptionAllMessages(e)));
	}
}
