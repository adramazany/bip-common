package bip.etl;

import java.util.Dictionary;
import java.util.Hashtable;

import aip.util.AIPWebUserParam;


public class ProcessParam  {
	String reqCode;
	String tablename;
	boolean isfullprocess;
//	String codemelli;
//	String edare;
	String tarikhaz;
	String tarikhta;
	
	boolean ignoreErrorAndLog;
	
	AIPWebUserParam webUserParam;
	
	String tableId;

	Hashtable<String, String> otherParams=new Hashtable<String, String>();
	String otherQueryCount;
	String otherQueryEtl;
	
	
	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getReqCode() {
		return reqCode;
	}

	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}

	public boolean getIsfullprocess() {
		return isfullprocess;
	}

	public void setIsfullprocess(boolean isfullprocess) {
		this.isfullprocess = isfullprocess;
	}

/*	public String getCodemelli() {
		return codemelli;
	}

	public void setCodemelli(String codemelli) {
		this.codemelli = codemelli;
	}
*/
	public String getTarikhaz() {
		return tarikhaz;
	}

	public void setTarikhaz(String tarikhaz) {
		this.tarikhaz = tarikhaz;
	}

	public String getTarikhta() {
		return tarikhta;
	}

	public void setTarikhta(String tarikhta) {
		this.tarikhta = tarikhta;
	}

/*	public String getEdare() {
		return edare;
	}

	public void setEdare(String edare) {
		this.edare = edare;
	}
*/
	public boolean isIgnoreErrorAndLog() {
		return ignoreErrorAndLog;
	}

	public boolean getIgnoreErrorAndLog() {
		return ignoreErrorAndLog;
	}

	public void setIgnoreErrorAndLog(boolean ignoreErrorAndLog) {
		this.ignoreErrorAndLog = ignoreErrorAndLog;
	}

	public AIPWebUserParam getWebUserParam() {
		return webUserParam;
	}

	public void setWebUserParam(AIPWebUserParam webUserParam) {
		this.webUserParam = webUserParam;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public Hashtable<String, String> getOtherParams() {
		return otherParams;
	}

	public void setOtherParams(Hashtable<String, String> otherParams) {
		this.otherParams = otherParams;
	}

	public String getOtherQueryCount() {
		return otherQueryCount;
	}

	public void setOtherQueryCount(String otherQueryCount) {
		this.otherQueryCount = otherQueryCount;
	}

	public String getOtherQueryEtl() {
		return otherQueryEtl;
	}

	public void setOtherQueryEtl(String otherQueryEtl) {
		this.otherQueryEtl = otherQueryEtl;
	}

	
	
	

}
