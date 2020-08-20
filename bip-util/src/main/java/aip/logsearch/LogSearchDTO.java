package aip.logsearch;

import aip.orm.AIPBaseEntity;

public class LogSearchDTO extends AIPBaseEntity {

	private java.lang.Integer id;
	private java.lang.Integer f_logsearchsentenceid;
	private java.lang.String logdate;
	private java.lang.String time;
	private java.lang.String username;
	private java.lang.String remoteip;
	private java.lang.String remotehost;
	private java.lang.Integer remoteport;
	private java.lang.String authtype;
	private java.lang.String sessionid;
	private java.lang.String userprincipal;
	
	private java.lang.String sentence;

	public java.lang.String getLogdate() {
		return logdate;
	}
	public void setLogdate(java.lang.String logdate) {
		this.logdate = logdate;
	}
	public java.lang.String getTime() {
		return time;
	}
	public void setTime(java.lang.String time) {
		this.time = time;
	}
	public java.lang.Integer getF_logsearchsentenceid() {
		return f_logsearchsentenceid;
	}
	public void setF_logsearchsentenceid(java.lang.Integer f_logsearchsentenceid) {
		this.f_logsearchsentenceid = f_logsearchsentenceid;
	}
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public java.lang.String getSentence() {
		return sentence;
	}
	public void setSentence(java.lang.String sentence) {
		this.sentence = sentence;
	}
	public java.lang.String getUsername() {
		return username;
	}
	public void setUsername(java.lang.String username) {
		this.username = username;
	}
	public java.lang.String getRemoteip() {
		return remoteip;
	}
	public void setRemoteip(java.lang.String remoteip) {
		this.remoteip = remoteip;
	}
	public java.lang.String getRemotehost() {
		return remotehost;
	}
	public void setRemotehost(java.lang.String remotehost) {
		this.remotehost = remotehost;
	}
	public Integer getRemoteport() {
		return remoteport;
	}
	public void setRemoteport(Integer remoteport) {
		this.remoteport = remoteport;
	}
	public java.lang.String getAuthtype() {
		return authtype;
	}
	public void setAuthtype(java.lang.String authtype) {
		this.authtype = authtype;
	}
	public java.lang.String getSessionid() {
		return sessionid;
	}
	public void setSessionid(java.lang.String sessionid) {
		this.sessionid = sessionid;
	}
	public java.lang.String getUserprincipal() {
		return userprincipal;
	}
	public void setUserprincipal(java.lang.String userprincipal) {
		this.userprincipal = userprincipal;
	}

	

}
