package aip.logsearch;

import aip.report.AIPReportParam;

public class LogSearchStatisticLSTParam  extends AIPReportParam {

	private java.lang.String logdatefrom;
	private java.lang.String logdateto;
	private java.lang.String filter;
	private java.lang.String username;
	
	public java.lang.String getUsername() {
		return username;
	}
	public void setUsername(java.lang.String username) {
		this.username = username;
	}
	public java.lang.String getLogdatefrom() {
		return logdatefrom;
	}
	public void setLogdatefrom(java.lang.String logdatefrom) {
		this.logdatefrom = logdatefrom;
	}
	public java.lang.String getLogdateto() {
		return logdateto;
	}
	public void setLogdateto(java.lang.String logdateto) {
		this.logdateto = logdateto;
	}
	public java.lang.String getFilter() {
		return filter;
	}
	public void setFilter(java.lang.String filter) {
		this.filter = filter;
	}
	

}
