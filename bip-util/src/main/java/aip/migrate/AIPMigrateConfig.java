package aip.migrate;

public class AIPMigrateConfig {
	String srcDriver;
	String srcURL;
	String srcUser;
	String srcPass;

	String destDriver;
	String destURL;
	String destUser;
	String destPass;
	
	/*
	 * other
	 */
	String path;
	
	int transferPrintLength;
	
	String cnvCharsSrc;
	String cnvCharsDest;
	
	public String getSrcDriver() {
		return srcDriver;
	}
	public void setSrcDriver(String srcDriver) {
		this.srcDriver = srcDriver;
	}
	public String getSrcURL() {
		return srcURL;
	}
	public void setSrcURL(String srcURL) {
		this.srcURL = srcURL;
	}
	public String getSrcUser() {
		return srcUser;
	}
	public void setSrcUser(String srcUser) {
		this.srcUser = srcUser;
	}
	public String getSrcPass() {
		return srcPass;
	}
	public void setSrcPass(String srcPass) {
		this.srcPass = srcPass;
	}
	public String getDestDriver() {
		return destDriver;
	}
	public void setDestDriver(String destDriver) {
		this.destDriver = destDriver;
	}
	public String getDestURL() {
		return destURL;
	}
	public void setDestURL(String destURL) {
		this.destURL = destURL;
	}
	public String getDestUser() {
		return destUser;
	}
	public void setDestUser(String destUser) {
		this.destUser = destUser;
	}
	public String getDestPass() {
		return destPass;
	}
	public void setDestPass(String destPass) {
		this.destPass = destPass;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getTransferPrintLength() {
		return transferPrintLength;
	}
	public void setTransferPrintLength(int transferPrintLength) {
		this.transferPrintLength = transferPrintLength;
	}
	public String getCnvCharsSrc() {
		return cnvCharsSrc;
	}
	public void setCnvCharsSrc(String cnvCharsSrc) {
		this.cnvCharsSrc = cnvCharsSrc;
	}
	public String getCnvCharsDest() {
		return cnvCharsDest;
	}
	public void setCnvCharsDest(String cnvCharsDest) {
		this.cnvCharsDest = cnvCharsDest;
	}
	
	
	

}
