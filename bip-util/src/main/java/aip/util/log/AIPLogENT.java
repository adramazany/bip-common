package aip.util.log;

import java.io.Serializable;

import aip.util.AIPWebUserParam;
import aip.util.DateConvert;
import aip.util.NVL;

public class AIPLogENT implements Serializable {

	private static final long serialVersionUID = 1L;
	double id;//ID
	String date;//تاریخ
	String time;//ساعت
	String username;//کاربر
	String remoteip;//آدرس اینترنتی
	String uc_name;//موضوع
	String uc_level;//سطح
//	String uc_res = AIPENUM_LOGLEVEL.INFO.val();
	String uc_id;//شناسه
	String uc_no;//شماره
	String uc_op;//متد
//	String uc_oldcriticaldata;//داده های مهم  //use desc instead
	StringBuffer uc_desc=new StringBuffer();//شرح
//	String uc_msg;//پیغام //use desc instead
//	String uc_action_reqcode_forward;//عملیات //use desc instead
//	String puc_name;//موضوع بالاتر //use desc instead
//	String puc_id;//شناسه بالاتر //use desc instead
//	String puc_no;//شماره بالاتر //use desc instead

	AIPLogENT() {
		super();
		this.date = DateConvert.getTodayJalali();
		this.time = NVL.getNowTime();
		this.uc_level = AIPLog.LEVEL_INFO;
	}
//
//	public AIPLogENT(AIPWebUserParam webUser,String uc_name){
//		this(webUser, uc_name, null);
//	}
	
	private AIPLogENT(AIPWebUserParam webUser,String uc_name, String uc_op) {
		this(webUser, uc_name, null, null, null, uc_op, null);
	}

//	public AIPLogENT(AIPWebUserParam webUser,String uc_name, Object uc_id,String uc_no) {
//		this(webUser, uc_name, null,NVL.getStringNull(uc_id), uc_no,NVL.isEmpty(NVL.getStringNull(uc_id))?AIPLog.OP_UPDATE:AIPLog.OP_INSERT, null);
//	}

	public AIPLogENT(AIPWebUserParam webUser, String uc_name, String uc_op, Object uc_id,Object uc_no){
		this(webUser, uc_name, null,NVL.getStringNull(uc_id), NVL.getStringNull(uc_no), uc_op, null);
	}
	private AIPLogENT(AIPWebUserParam webUser, String uc_name, String uc_level, String uc_id,String uc_no, String uc_op, String uc_desc) {
//		this.date = DateConvert.getTodayJalali();
//		this.time = NVL.getNowTime();
		this.username = webUser.getRemoteUser();
		this.remoteip = webUser.getRemoteAddr();
		this.uc_name = uc_name;
		this.uc_level = uc_level;
		this.uc_id = uc_id;
		this.uc_no = uc_no;
		this.uc_op = uc_op;
		this.uc_desc = new StringBuffer( NVL.getString(uc_desc) );
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

	public String getUc_level() {
		return uc_level;
	}

	public void setUc_level(String uc_level) {
		this.uc_level = uc_level;
	}

	public String getUc_id() {
		return uc_id;
	}

	public void setUc_id(String uc_id) {
		this.uc_id = uc_id;
	}
	public void setUc_id(Object uc_id) {
		this.uc_id = NVL.getString(uc_id);
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
		return uc_desc.toString();
	}

	public void setUc_desc(String uc_desc) {
		this.uc_desc = new StringBuffer(NVL.getString(uc_desc));
	}
	public void addUc_desc(String add2uc_desc) {
		this.uc_desc.append("\r\n");
		this.uc_desc.append(add2uc_desc);
	}
	public void addUc_error(String add2uc_desc) {
		this.uc_desc.append("\r\n");
		this.uc_desc.append("اشکال:");
		this.uc_desc.append(add2uc_desc);
		this.uc_level=AIPLog.LEVEL_ERROR;
	}
	public void addUc_error(Exception ex) {
		addUc_error(ex.getMessage());
	}

	public void addUc_warning(String add2uc_desc) {
		this.uc_desc.append("\r\n");
		this.uc_desc.append("توجه:");
		this.uc_desc.append(add2uc_desc);
		this.uc_level=AIPLog.LEVEL_WARN;
	}
	public void addUc_warning(Exception ex) {
		addUc_warning(ex.getMessage());
	}


}
