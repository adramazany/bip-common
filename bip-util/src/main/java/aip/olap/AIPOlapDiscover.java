package aip.olap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapDatabaseMetaData;
import org.olap4j.OlapStatement;

import aip.util.AIPException;

public class AIPOlapDiscover {

	String url="http://192.168.0.240:81/olap/msmdpump.dll";
	String catalog="NIOPDC_BI_Ver2";
	String username="";
	String password="";

	public AIPOlapDiscover(String url, String catalog, String username, String password) {
		super();
		this.url = url;
		this.catalog = catalog;
		this.username = username;
		this.password = password;
		System.out.println("AIPOlapDiscover.AIPOlapDiscover():url="+url+", catalog="+catalog+", username="+username+", password="+password);
	}

	public void discoverCat()throws AIPException{
		OlapConnection olapConnection = null;
		try {
			Class.forName("org.olap4j.driver.xmla.XmlaOlap4jDriver");
//			Connection connection = DriverManager.getConnection("jdbc:xmla:Server="+url+";"
//					+ "Catalog="+catalog,username,password );
			Properties props = new Properties(); 
			props.put("user",username);
			props.put("password",password);
			props.put("charSet","UTF-8");
			Connection connection = DriverManager.getConnection("jdbc:xmla:Server="+url+";"+ "Catalog="+catalog,props );
			
			olapConnection = connection.unwrap(OlapConnection.class);
			
			OlapDatabaseMetaData metaData = olapConnection.getMetaData();
			//metaData.getDimensions(arg0, arg1, arg2, arg3)
			//metaData.

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AIPException(ex);
		} finally {
			try {
				if (olapConnection != null)
					olapConnection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {
		AIPOlapDiscover olap = new  AIPOlapDiscover("http://192.168.0.240:81/olap/msmdpump.dll","NIOPDC_BI_Ver2","Administrator", "M2221294M");
		
		//olap.discoverCat();
		
	}
}
