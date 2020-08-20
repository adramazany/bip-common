package aip.generator.struts.list;

import aip.util.NVL;

public class AIPStrutsReportListWithToolbarParam {
	public static final String IDGEN_ASSIGNED = "assigned";
	public static final String IDGEN_IDENTITY = "identity";
	public static final String IDGEN_SEQUENCE = "sequence";
	
	public static final int GENERATION_HBM = 1;
	public static final int GENERATION_ENTITY = 2;
	public static final int GENERATION_INTERFACE = 4;
	public static final int GENERATION_PARAM = 8;
	public static final int GENERATION_LST = 16;
	public static final int GENERATION_COMBOLST = 32;
	public static final int GENERATION_DAO = 64;
	public static final int GENERATION_ACTION = 128;
	public static final int GENERATION_TILES = 256;
	public static final int GENERATION_JSP = 512;
	public static final int GENERATION_JSPPARAM = 1024;
	public static final int GEN_UPD_HIBERNATE = 2048;
	public static final int GEN_UPD_STRUTS = 4096;
	public static final int GEN_UPD_TILES = 8192;
	public static final int GEN_ADD_ACTION_GETENTITY = 16384;
	public static final int GEN_DAO_GETENTITY = 32768;
	public static final int GEN_DAO_SAVEENTITY = 65536;
	public static final int GEN_ACTION_EDITENTITY = 131072;
	public static final int GEN_ACTION_SAVEENTITY = 262144;
	public static final int GEN_STRUTSFORWARD_EDITENTITY = 524288;
	public static final int GEN_JSP_EDITENTITY = 1048576;

	public static final int GENERATION_HBM_ENT = 2097152;
	
	//...
	static final int GENERATION_GENERATOR = 67108864;
	
	public static final int GENERATION_FULL = GENERATION_HBM+GENERATION_ENTITY+GENERATION_INTERFACE+GENERATION_PARAM
		+GENERATION_LST+GENERATION_COMBOLST+GENERATION_DAO+GENERATION_ACTION+GENERATION_TILES+GENERATION_JSP+GENERATION_JSPPARAM
		+GEN_UPD_HIBERNATE+GEN_UPD_STRUTS+GEN_UPD_TILES;
	
	String name;
	String title;
	String className;

	String destPath;
	String jspPath;
	String packagePath;

	String tableName;
	String loaderSQL;

	String idColumn="id";
	String idGeneratorClass=IDGEN_IDENTITY;
	
	String connectionDriver;
	String connectionString;
	String connectionUser;
	String connectionPassword;
	
	String[] paramColumns;
	String[] paramColumnsTitle;
	String[][] visibleColumns;
	String[][] paramCombos;
	int generation;
	
	
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
	String getClassName(){
		if(NVL.isEmpty(className)){
			return name+"DTO";
		}else{
			return className;
		}
	}
	String getClassFullName(){
		String classFullName = getPackagePath()+"."+getClassName();
		return classFullName;
	}
	boolean hasGeneration(int generation){
		return (this.generation & generation)==generation;
	}
	public String getHibernateCfgPath() {
		int pos = destPath.indexOf("/src/");
		if(pos>-1){
			String p = destPath.substring(0,pos+5)+"hibernate.cfg.xml";
			return p;
		}else{
			return destPath+"/hibernate.cfg.xml";
		}
	}
	public String getStrutsCfgPath() {
		int pos = destPath.indexOf("/src/");
		if(pos>-1){
			String p = destPath.substring(0,pos)+"/WebRoot/WEB-INF/struts-config.xml";
			return p;
		}else{
			return null;
		}
	}
	public String getTilesCfgPath() {
		int pos = destPath.indexOf("/src/");
		if(pos>-1){
			String p = destPath.substring(0,pos)+"/WebRoot/WEB-INF/struts-config-tiles.xml";
			return p;
		}else{
			return null;
		}
	}
	public String getTilesDefsPath() {
		int pos = destPath.indexOf("/src/");
		if(pos>-1){
			String p = destPath.substring(0,pos)+"/WebRoot/WEB-INF/tiles-defs.xml";
			return p;
		}else{
			return null;
		}
	}
	

	
	
	
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDestPath() {
		return destPath;
	}
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}
	public String getLoaderSQL() {
		return loaderSQL;
	}
	public void setLoaderSQL(String loaderSQL) {
		this.loaderSQL = loaderSQL;
	}
	public String getConnectionDriver() {
		return connectionDriver;
	}
	public void setConnectionDriver(String connectionDriver) {
		this.connectionDriver = connectionDriver;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	public String getConnectionUser() {
		return connectionUser;
	}
	public void setConnectionUser(String connectionUser) {
		this.connectionUser = connectionUser;
	}
	public String getConnectionPassword() {
		return connectionPassword;
	}
	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}
	public String[] getParamColumns() {
		return paramColumns;
	}
	public void setParamColumns(String[] paramColumns) {
		this.paramColumns = paramColumns;
	}
	public String[][] getVisibleColumns() {
		return visibleColumns;
	}
	public void setVisibleColumns(String[][] visibleColumns) {
		this.visibleColumns = visibleColumns;
	}
	public String[][] getParamCombos() {
		return paramCombos;
	}
	public void setParamCombos(String[][] paramCombos) {
		this.paramCombos = paramCombos;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	public String getIdColumn() {
		return idColumn;
	}
	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}
	public String getIdGeneratorClass() {
		return idGeneratorClass;
	}
	public void setIdGeneratorClass(String idGeneratorClass) {
		this.idGeneratorClass = idGeneratorClass;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getGeneration() {
		return generation;
	}
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJspPath() {
		return jspPath;
	}
	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}
	public String[] getParamColumnsTitle() {
		return paramColumnsTitle;
	}
	public void setParamColumnsTitle(String[] paramColumnsTitle) {
		this.paramColumnsTitle = paramColumnsTitle;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
