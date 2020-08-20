package aip.generator.dbhbm;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import aip.generator.struts.list.AIPStrutsReportListWithToolbarGEN;
import aip.generator.struts.list.AIPStrutsReportListWithToolbarParam;
import aip.util.AIPException;
import aip.util.AIPUtil;
import aip.db.AIPDBTable;
import aip.db.AIPDBUtil;

public class GenerateDatabaseHbms {

	public static void main(String[] args) {
		GenerateDatabaseHbms gen = new GenerateDatabaseHbms();
		try {
			if(args.length!=1){
				System.out.println("AIPMigrate syntax:\n" +
						"\t java aip.generator.dbhbm.GenerateDatabaseHbms package => generate all entities for package aip.sabtbi.db.\n" +
						"\t ex:java aip.generator.dbhbm.GenerateDatabaseHbms aip.sabtbi.db => generate all entities for package aip.sabtbi.db.\n" +
						"\n");
			}else{
				gen.generate(args[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void generate(String packagename) throws Exception{
		Connection cn = AIPDBUtil.getDataSourceConnection("jdbc/generatedatabasehbmsds");
		
		List<AIPDBTable> tables = AIPDBUtil.getDBTables(cn);
		
		Properties propDS = new Properties();
		propDS.load(new FileInputStream("jdbc.generatedatabasehbmsds.cfg"));
		
		
		for (int i = 0; i < tables.size(); i++) {
			AIPDBTable table = tables.get(i);

			String name =AIPUtil.change2Capital(table.getTablename().toLowerCase().replaceAll("dim_", "").replaceAll("fact_", ""));
			String tablename = table.getTablename();
			String classname = name+"ENT";
			String filename = classname+".hbm.xml";
			String fullclassname = packagename+"."+classname;

			System.out.println("table="+tablename);
			
			AIPStrutsReportListWithToolbarGEN gen = new AIPStrutsReportListWithToolbarGEN();
			AIPStrutsReportListWithToolbarParam param=new AIPStrutsReportListWithToolbarParam();
			param.setName( name );
			param.setClassName(classname);
			//param.setTitle("محل جغرافیایی");
			param.setDestPath("./database/");
			param.setPackagePath(packagename);
			//param.setJspPath("/home/adl/java/workspace/TestJSF2Primefaces/src/aip/sabtbi/database");
			param.setTableName(tablename);
			param.setLoaderSQL("select * from "+tablename+" WHERE id=?");
			
			param.setIdColumn("id");
			param.setIdGeneratorClass("identity");
			
			
			param.setConnectionDriver(propDS.getProperty("driverClassName"));//"com.microsoft.sqlserver.jdbc.SQLServerDriver");
			param.setConnectionString(propDS.getProperty("url"));//"jdbc:sqlserver://192.168.0.71:1433;databaseName=aipsabtbidw");
			param.setConnectionUser(propDS.getProperty("username"));//"nhag");
			param.setConnectionPassword(propDS.getProperty("password"));//"nhag");
					/*
			 * columnName should exists in loaderSQL
			 * todo: count and ids querys generate without joins if exists manually handled
			 */
			param.setParamColumns(new String[]{ 
			});
			param.setParamColumnsTitle(new String[]{
					});
			param.setVisibleColumns(new String[][]{
			});
			param.setParamCombos(new String[][]{
			});
			
			param.setGeneration (param.GENERATION_HBM_ENT+param.GENERATION_ENTITY +param.GEN_UPD_HIBERNATE);
			
			try {
				gen.generateCode(param);
			} catch (AIPException e) {
				e.printStackTrace();
			}
			
		}
		
	}


	
	
	/*******************************************************************/
	
	private String getColumnClassName(ResultSetMetaData meta, int column) throws SQLException {
		String className = meta.getColumnClassName(column);
		if(className.indexOf("BigDecimal")>-1){
			int scale = meta.getScale(column);
			int precision = meta.getPrecision(column);
			if(scale>0){
				className="java.lang.Double";
			}else if(precision>9){
				className="java.lang.Long";
			}else{
				className="java.lang.Integer";
			}
		}
		return className;
	}
	
}
