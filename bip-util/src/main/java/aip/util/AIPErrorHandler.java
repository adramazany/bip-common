package aip.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import aip.util.AIPException;

public class AIPErrorHandler {

	public static void setAttributeErrorMessage(HttpServletRequest request,Exception e, String error) {
		_setAttributeErrorMessage(request, e, error);
	}
	public static void setAttributeErrorMessage(ServletRequest request,Exception e, String error) {
		_setAttributeErrorMessage(request, e, error);
	}
	private static void _setAttributeErrorMessage(ServletRequest request,Exception e, String error) {
		
		StringBuffer errorMessage = new StringBuffer();
		
		String oldErrorMessage =  NVL.getString( request.getAttribute("errorMessage"));
		String exceptionMessage = NVL.getString( AIPUtil.getExceptionAllMessages(e));
		
		if(!NVL.isEmpty(error) && exceptionMessage.indexOf(error)<0 && oldErrorMessage.indexOf(error)<0){
			errorMessage.append(error);
		}
		if(!NVL.isEmpty(exceptionMessage)){
			if(errorMessage.length()>0)errorMessage.append("<br/>");
			errorMessage.append(exceptionMessage);
		}
		if(!NVL.isEmpty(oldErrorMessage)){
			if(errorMessage.length()>0)errorMessage.append("<br/>");
			errorMessage.append(oldErrorMessage);
		}

		request.setAttribute("errorMessage",errorMessage.toString());
	}
	
}