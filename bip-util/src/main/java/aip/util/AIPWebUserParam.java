package aip.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.MultipartRequestWrapper;

import aip.util.NVL;

public class AIPWebUserParam {
   private String remoteUser ;
   private String remoteAddr;
   private String remoteHost;
   private int remotePort;
   private String  authType;
   private String requestedSessionId;
   private String userPrincipal;
public String getRemoteUser() {
	return remoteUser;
}
public void setRemoteUser(String remoteUser) {
	this.remoteUser = remoteUser;
}
public String getRemoteAddr() {
	return remoteAddr;
}
public void setRemoteAddr(String remoteAddr) {
	this.remoteAddr = remoteAddr;
}
public String getRemoteHost() {
	return remoteHost;
}
public void setRemoteHost(String remoteHost) {
	this.remoteHost = remoteHost;
}
public int getRemotePort() {
	return remotePort;
}
public void setRemotePort(int remotePort) {
	this.remotePort = remotePort;
}
public String getAuthType() {
	return authType;
}
public void setAuthType(String authType) {
	this.authType = authType;
}
public String getRequestedSessionId() {
	return requestedSessionId;
}
public void setRequestedSessionId(String requestedSessionId) {
	this.requestedSessionId = requestedSessionId;
}
public String getUserPrincipal() {
	return userPrincipal;
}
public void setUserPrincipal(String userPrincipal) {
	this.userPrincipal = userPrincipal;
}
 private AIPWebUserParam(){};
 public AIPWebUserParam(HttpServletRequest  request) {
	 this.setAuthType(request.getAuthType());
	 this.setRemoteAddr(request.getRemoteAddr());
	 this.setRemoteHost(request.getRemoteHost());
	 
	 if(request.getRemoteUser()==null){
		 this.setRemoteUser(getUserInSession(request));
	 }else{
		 this.setRemoteUser(request.getRemoteUser());
	 }
	 
	 
	 this.setRequestedSessionId(request.getRequestedSessionId());
	 this.setUserPrincipal( NVL.getString(request.getUserPrincipal()));
	if(!(request instanceof MultipartRequestWrapper)){
		 this.setRemotePort(request.getRemotePort());
	}
 }
 
 public AIPWebUserParam(String userName) {
	 this.setRemoteUser(userName);
 }
 
 public static void setUserInSession(HttpServletRequest request,String username){
	 request.getSession(true).setAttribute("j_username", username);
 }
 public static String getUserInSession(HttpServletRequest request){
	 return NVL.getStringNull(request.getSession().getAttribute("j_username"));
 }
 public static void removeUserInSession(HttpServletRequest request){
	 request.getSession().removeAttribute("j_username");
 }
}
