package aip.logsearch;

import aip.report.AIPReportParam;

public class LogSearchLSTParam  extends AIPReportParam {
	String logdatefrom;
	String logdateto;
	String filter;
	String username;
	public String getLogdatefrom() {
		return logdatefrom;
	}
	public void setLogdatefrom(String logdatefrom) {
		this.logdatefrom = logdatefrom;
	}
	public String getLogdateto() {
		return logdateto;
	}
	public void setLogdateto(String logdateto) {
		this.logdateto = logdateto;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	


}
