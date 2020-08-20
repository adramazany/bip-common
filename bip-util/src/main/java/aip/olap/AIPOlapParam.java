package aip.olap;

import aip.orm.AIPBaseParam;

public class AIPOlapParam extends AIPBaseParam{
	
	String reqCode;
	String mdx;
	String username;
	String password;
	String url;
	String catalog;

	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getMdx() {
		return mdx;
	}
	public void setMdx(String mdx) {
		this.mdx = mdx;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getReqCode() {
		return reqCode;
	}
	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}
	
	

}
