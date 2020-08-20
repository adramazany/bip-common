package aip.generator.ajax.multiselect;

import aip.util.NVL;

public class AIPAjaxMultiSelectGeneratorParam {

	public static final int GEN_DAO_GETBASES = 1;
	public static final int GEN_DAO_GETDETAILS = 2;
	public static final int GEN_DAO_SAVEDETAILS = 4;
	public static final int GEN_ACTION_GETDETAILSAJAX = 8;
	public static final int GEN_STRUTS_FORWARD_DETAILS = 16;
	public static final int GEN_JSP_DETAILS = 32;
	public static final int GEN_ACTION_SAVEDETAILS = 64;
	//...
	static final int GEN_GENERATOR = 65536;
	
	public static final int GENERATION_FULL = GEN_DAO_GETBASES+GEN_DAO_GETDETAILS
					+GEN_DAO_SAVEDETAILS+GEN_ACTION_GETDETAILSAJAX+GEN_STRUTS_FORWARD_DETAILS
					+GEN_JSP_DETAILS+GEN_ACTION_SAVEDETAILS;
	
	String master;
	String base;
	String detail;
	
	String baseEntityClass;
	String detailEntityClass;
	
	String title;
	String destPath;
	String jspPath;
//	String connectionDriver;
//	String connectionString;
//	String connectionUser;
//	String connectionPassword;
	int generation;
	
	String masterIdArg;
	String masterIdArgType;
	String baseIdArg;
	String detailFk2baseArg;
	String detailFk2baseArgType;
	String baseTitle;
	String packagePath;
	


	
	public String getStrutsCfgPath() {
		int pos = destPath.indexOf("/src/");
		if(pos>-1){
			String p = destPath.substring(0,pos)+"/WebRoot/WEB-INF/struts-config.xml";
			return p;
		}else{
			return null;
		}
	}
	boolean hasGeneration(int generation){
		return (this.generation & generation)==generation;
	}
	public String getPackagePath() {
		if(NVL.isEmpty(packagePath) && !NVL.isEmpty(destPath)){
			int pos = destPath.indexOf("/src/");
			if(pos>-1){
				String p = destPath.substring(pos+5);
				p=p.replace('/', '.');
				return p;
			}else{
				return packagePath;
			}
		}else{
			return packagePath;
		}
	}





	public String getMaster() {
		return master;
	}





	public void setMaster(String master) {
		this.master = master;
	}





	public String getBase() {
		return base;
	}





	public void setBase(String base) {
		this.base = base;
	}





	public String getDetail() {
		return detail;
	}





	public void setDetail(String detail) {
		this.detail = detail;
	}





	public String getBaseEntityClass() {
		return baseEntityClass;
	}





	public void setBaseEntityClass(String baseEntityClass) {
		this.baseEntityClass = baseEntityClass;
	}





	public String getTitle() {
		return title;
	}





	public void setTitle(String title) {
		this.title = title;
	}





	public String getDestPath() {
		return destPath;
	}





	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}





	public String getJspPath() {
		return jspPath;
	}





	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}





//	public String getConnectionDriver() {
//		return connectionDriver;
//	}
//
//
//
//
//
//	public void setConnectionDriver(String connectionDriver) {
//		this.connectionDriver = connectionDriver;
//	}
//
//
//
//
//
//	public String getConnectionString() {
//		return connectionString;
//	}
//
//
//
//
//
//	public void setConnectionString(String connectionString) {
//		this.connectionString = connectionString;
//	}
//
//
//
//
//
//	public String getConnectionUser() {
//		return connectionUser;
//	}
//
//
//
//
//
//	public void setConnectionUser(String connectionUser) {
//		this.connectionUser = connectionUser;
//	}
//
//
//
//
//
//	public String getConnectionPassword() {
//		return connectionPassword;
//	}
//
//
//
//
//
//	public void setConnectionPassword(String connectionPassword) {
//		this.connectionPassword = connectionPassword;
//	}





	public int getGeneration() {
		return generation;
	}





	public void setGeneration(int generation) {
		this.generation = generation;
	}





	public String getDetailEntityClass() {
		return detailEntityClass;
	}





	public void setDetailEntityClass(String detailEntityClass) {
		this.detailEntityClass = detailEntityClass;
	}
	public String getMasterIdArg() {
		return masterIdArg;
	}
	public void setMasterIdArg(String masterIdArg) {
		this.masterIdArg = masterIdArg;
	}
	public String getMasterIdArgType() {
		return masterIdArgType;
	}
	public void setMasterIdArgType(String masterIdArgType) {
		this.masterIdArgType = masterIdArgType;
	}
	public String getDetailFk2baseArg() {
		return detailFk2baseArg;
	}
	public void setDetailFk2baseArg(String detailFk2baseArg) {
		this.detailFk2baseArg = detailFk2baseArg;
	}
	public String getBaseTitle() {
		return baseTitle;
	}
	public void setBaseTitle(String baseTitle) {
		this.baseTitle = baseTitle;
	}
	public String getDetailFk2baseArgType() {
		return detailFk2baseArgType;
	}
	public void setDetailFk2baseArgType(String detailFk2baseArgType) {
		this.detailFk2baseArgType = detailFk2baseArgType;
	}
	public String getBaseIdArg() {
		return baseIdArg;
	}
	public void setBaseIdArg(String baseIdArg) {
		this.baseIdArg = baseIdArg;
	}





	
	
}
