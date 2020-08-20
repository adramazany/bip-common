package aip.util.log;

import aip.report.AIPReportParam;

public class AIPLogParam extends AIPReportParam {
	
    private String fromDate;
    private String fromHour;
    private String toDate;
    private String toHour;
    private String username;
    private String uc_name;
    private String uc_level;
    private String uc_no;
    private String uc_op;
    private String uc_desc;
	
    public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getFromHour() {
		return fromHour;
	}
	public void setFromHour(String fromHour) {
		this.fromHour = fromHour;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getToHour() {
		return toHour;
	}
	public void setToHour(String toHour) {
		this.toHour = toHour;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUc_name() {
		return uc_name;
	}
	public void setUc_name(String uc_name) {
		this.uc_name = uc_name;
	}
	public String getUc_no() {
		return uc_no;
	}
	public void setUc_no(String uc_no) {
		this.uc_no = uc_no;
	}
	public String getUc_op() {
		return uc_op;
	}
	public void setUc_op(String uc_op) {
		this.uc_op = uc_op;
	}
	public String getUc_desc() {
		return uc_desc;
	}
	public void setUc_desc(String uc_desc) {
		this.uc_desc = uc_desc;
	}
	public String getUc_level() {
		return uc_level;
	}
	public void setUc_level(String uc_level) {
		this.uc_level = uc_level;
	}
	
}
