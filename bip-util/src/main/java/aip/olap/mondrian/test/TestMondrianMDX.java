package aip.olap.mondrian.test;

import java.sql.DriverManager;

import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapStatement;

import aip.olap.AIPOlapUtil;
import aip.olap.export.AIPOlapExportJSON;

public class TestMondrianMDX {
	static String mdx = "with member Measures.AAA as Measures.UnitSales*100,format_string='#,#.00'"
			+ " select {Measures.AAA} on columns "
			+ " ,{Customers.country.members} on rows "
			+ " from Sales ";

	public static void main1(String[] args) {
		try {
			Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
			OlapConnection cn = (OlapConnection) DriverManager.getConnection("jdbc:mondrian:Jdbc=jdbc:mysql://localhost:3306/foodmart;"
					+ "JdbcUser=root;JdbcPassword=aippia;"
					+ "Catalog=e:/olap/foodmart-mysql-schema.xml;"
					+ "Role=");
			cn = cn.unwrap(OlapConnection.class);
			OlapStatement stmt = cn.createStatement();
			
			
			CellSet cellSet = stmt.executeOlapQuery(mdx);
			
			AIPOlapExportJSON export = new AIPOlapExportJSON();
			export.export(cellSet, System.out);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		try {
			CellSet cellSet = AIPOlapUtil.executeMdx("foodmartcube", mdx);
			
			AIPOlapExportJSON export = new AIPOlapExportJSON();
			export.export(cellSet, System.out);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
