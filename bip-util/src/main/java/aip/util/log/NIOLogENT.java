package aip.util.log;

import java.io.Serializable;

public class NIOLogENT implements Serializable {

	private static final long serialVersionUID = 1L;
	double id;//ID
	String date;//تاریخ
	String time;//ساعت
	String username;//کاربر
	String remoteip;//آدرس اینترنتی
	String uc_name;//موضوع
	String uc_id;//شناسه
	String uc_no;//شماره
	String uc_op;//متد
	String uc_oldcriticaldata;//داده های مهم
	String uc_desc;//توضیحات
	String uc_msg;//پیغام
	String uc_res = AIPENUM_LOGLEVEL.INFO.val();
	String uc_action_reqcode_forward;//عملیات
	String puc_name;//موضوع بالاتر
	String puc_id;//شناسه بالاتر
	String puc_no;//شماره بالاتر
	
	public NIOLogENT() {}

	public NIOLogENT(Integer id, String date, String time, String username, String remoteip, String uc_name, String uc_id, String uc_no, String uc_op, String uc_oldcriticaldata, String uc_desc, String uc_msg, String uc_res, String uc_action_reqcode_forward, String puc_name, String puc_id, String puc_no) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
		this.username = username;
		this.remoteip = remoteip;
		this.uc_name = uc_name;
		this.uc_id = uc_id;
		this.uc_no = uc_no;
		this.uc_op = uc_op;
		this.uc_oldcriticaldata = uc_oldcriticaldata;
		this.uc_desc = uc_desc;
		this.uc_msg = uc_msg;
		this.uc_res = uc_res;
		this.uc_action_reqcode_forward = uc_action_reqcode_forward;
		this.puc_name = puc_name;
		this.puc_id = puc_id;
		this.puc_no = puc_no;
	}

	public NIOLogENT(String username, String remoteip, String uc_name, String uc_id, String uc_no, String uc_op, String uc_oldcriticaldata, String uc_desc, String uc_msg, String uc_res, String uc_action_reqcode_forward, String puc_name, String puc_id, String puc_no) {
		super();
		this.username = username;
		this.remoteip = remoteip;
		this.uc_name = uc_name;
		this.uc_id = uc_id;
		this.uc_no = uc_no;
		this.uc_op = uc_op;
		this.uc_oldcriticaldata = uc_oldcriticaldata;
		this.uc_desc = uc_desc;
		this.uc_msg = uc_msg;
		this.uc_res = uc_res;
		this.uc_action_reqcode_forward = uc_action_reqcode_forward;
		this.puc_name = puc_name;
		this.puc_id = puc_id;
		this.puc_no = puc_no;
	}

	public NIOLogENT(String username, String remoteip, String uc_name, String uc_op) {
		super();
		this.username = username;
		this.remoteip = remoteip;
		this.uc_name = uc_name;
		this.uc_op = uc_op;
	}

	public NIOLogENT(String username, String remoteip, String uc_name, String uc_id, String uc_no, String uc_op) {
		super();
		this.username = username;
		this.remoteip = remoteip;
		this.uc_name = uc_name;
		this.uc_id = uc_id;
		this.uc_no = uc_no;
		this.uc_op = uc_op;
	}

	public NIOLogENT(String username, String remoteip, String uc_name, String uc_id, String uc_no, String uc_op, String uc_oldcriticaldata) {
		super();
		this.username = username;
		this.remoteip = remoteip;
		this.uc_name = uc_name;
		this.uc_id = uc_id;
		this.uc_no = uc_no;
		this.uc_op = uc_op;
		this.uc_oldcriticaldata = uc_oldcriticaldata;
	}

	public NIOLogENT(String username, String remoteip, String uc_name, String uc_id, String uc_no, String uc_op, String uc_oldcriticaldata, String uc_desc, String uc_msg, String uc_res, String uc_action_reqcode_forward) {
		super();
		this.username = username;
		this.remoteip = remoteip;
		this.uc_name = uc_name;
		this.uc_id = uc_id;
		this.uc_no = uc_no;
		this.uc_op = uc_op;
		this.uc_oldcriticaldata = uc_oldcriticaldata;
		this.uc_desc = uc_desc;
		this.uc_msg = uc_msg;
		this.uc_res = uc_res;
		this.uc_action_reqcode_forward = uc_action_reqcode_forward;
	}
	
	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRemoteip() {
		return remoteip;
	}

	public void setRemoteip(String remoteip) {
		this.remoteip = remoteip;
	}

	public String getUc_name() {
		return uc_name;
	}

	public void setUc_name(String uc_name) {
		this.uc_name = uc_name;
	}

	public String getUc_id() {
		return uc_id;
	}

	public void setUc_id(String uc_id) {
		this.uc_id = uc_id;
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

	public String getUc_oldcriticaldata() {
		return uc_oldcriticaldata;
	}

	public void setUc_oldcriticaldata(String uc_oldcriticaldata) {
		this.uc_oldcriticaldata = uc_oldcriticaldata;
	}

	public String getUc_desc() {
		return uc_desc;
	}

	public void setUc_desc(String uc_desc) {
		this.uc_desc = uc_desc;
	}

	public String getUc_msg() {
		return uc_msg;
	}

	public void setUc_msg(String uc_msg) {
		this.uc_msg = uc_msg;
	}

	public String getUc_res() {
		return uc_res;
	}

	public void setUc_res(String uc_res) {
		this.uc_res = uc_res;
	}

	public String getUc_action_reqcode_forward() {
		return uc_action_reqcode_forward;
	}

	public void setUc_action_reqcode_forward(String uc_action_reqcode_forward) {
		this.uc_action_reqcode_forward = uc_action_reqcode_forward;
	}

	public String getPuc_name() {
		return puc_name;
	}

	public void setPuc_name(String puc_name) {
		this.puc_name = puc_name;
	}

	public String getPuc_id() {
		return puc_id;
	}

	public void setPuc_id(String puc_id) {
		this.puc_id = puc_id;
	}

	public String getPuc_no() {
		return puc_no;
	}

	public void setPuc_no(String puc_no) {
		this.puc_no = puc_no;
	}

}
