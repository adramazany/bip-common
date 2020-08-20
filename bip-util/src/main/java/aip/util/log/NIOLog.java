package aip.util.log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//import org.hibernate.cfg.Configuration;

import aip.util.AIPUtil;
import aip.util.DateConvert;
import aip.util.NVL;

public class NIOLog implements NIOLogInterface {

	public static NIOLog getInstance() {
		return new NIOLog();
	}

	public String extractChangedCriticalData(Object newObj, Object oldObj,
			String[] criticalFields) {
		// TODO Auto-generated method stub
		return null;
	}

	public NIOLogLST getLogLST(NIOLogParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	public void log(NIOLogENT logENT) {
		// TODO Auto-generated method stub
		
	}

/*	private NIOLog() {}
	
    private static org.hibernate.SessionFactory sessionFactory;
    private static String configFile = "/hibernate.cfg.xml";
    private  static Configuration configuration = new Configuration();
	private static Connection connection;
	
	private static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("com.mysql.jdbc.Driver");
				try {
					if (sessionFactory == null) {
						rebuildSessionFactory();
					}
					connection = DriverManager.getConnection(configuration.getProperty("connection.url"), configuration.getProperty("connection.username"), configuration.getProperty("connection.password"));
					System.out.println("done !");
				} catch (Exception e) {
					System.out.println("error !");
				}
		    	System.out.println("-----------------------------------------------------------------------------------------------------------------\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	
	public String extractChangedCriticalData(Object newObj, Object oldObj, String[] criticalFields) {
		String criticalData = "";
		for (int i = 0 ; i < criticalFields.length ; i++) {
			try {
				Object newFieldData = AIPUtil.invokeMulti(newObj, criticalFields[i]);
				Object oldFieldData = AIPUtil.invokeMulti(oldObj, criticalFields[i]);
				if (!newFieldData.equals(oldFieldData)) {
					criticalData += "("+criticalFields[i] + " Prev: " + oldFieldData + " New: " + newFieldData + ")";   
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return criticalData;
	}

	public NIOLogLST getLogLST(NIOLogParam param) {

		Integer totalRows = 0;
		int pageSize = 20; 
		int reqPage = 0;

		NIOLogLST logLST = new NIOLogLST();
		try {
			Statement stmt = getConnection().createStatement();
			
			String queryString4count = creatQueryString(true, param, 0, 0);
			ResultSet resultSet = stmt.executeQuery(queryString4count);
			if (resultSet.next()) {
				totalRows = resultSet.getInt(1);
			}
			if (totalRows != null) {
				logLST.setTotalRows(totalRows);
			}
			reqPage = param.getRequestPage() ;
			if (reqPage < 1) {
				reqPage = 1;
			}
			logLST.setCurrentPage( reqPage );
			logLST.setPageSize(pageSize);

			String queryString = creatQueryString(false, param, reqPage, pageSize);
			resultSet = stmt.executeQuery(queryString);
			List<NIOLogENT> logENTs = new ArrayList<NIOLogENT>();
			while (resultSet.next()) {
				NIOLogENT logENT = new NIOLogENT();
				logENT.setId(resultSet.getInt("id"));
				logENT.setDate(resultSet.getString("date"));
				logENT.setTime(resultSet.getString("time"));
				logENT.setUsername(resultSet.getString("username"));
				logENT.setRemoteip(resultSet.getString("remoteip"));
				logENT.setUc_name(resultSet.getString("uc_name"));
				logENT.setUc_id(getString(resultSet.getString("uc_id")));
				logENT.setUc_no(getString(resultSet.getString("uc_no")));
				logENT.setUc_op(getString(resultSet.getString("uc_op")));
				logENT.setUc_oldcriticaldata(getString(resultSet.getString("uc_oldcriticaldata")));
				logENT.setUc_desc(getString(resultSet.getString("uc_desc")));
				logENT.setUc_msg(getString(resultSet.getString("uc_msg")));
				logENT.setUc_res(getString(resultSet.getString("uc_res")));
				logENT.setUc_action_reqcode_forward(getString(resultSet.getString("uc_action_reqcode_forward")));
				logENT.setPuc_name(getString(resultSet.getString("puc_name")));
				logENT.setPuc_id(getString(resultSet.getString("puc_id")));
				logENT.setPuc_no(getString(resultSet.getString("puc_no")));
				logENTs.add(logENT);
			}
			logLST.setLogENTs(logENTs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return logLST;
	}

	private String creatQueryString(boolean is4count, NIOLogParam param, int reqPage, int pageSize) {
		String queryString = "";
		if (is4count) {
			queryString =	"SELECT COUNT(*) FROM aip_niolog ";
		} else {
			queryString =	"SELECT * FROM aip_niolog ";
		}
		boolean placeAND = false;
		if (param.getLevel() != null && !"ALL".equals(param.getLevel())) {
			queryString += " WHERE uc_res = '" + param.getLevel() + "'";
			placeAND = true;
		}
		if (param.getAction() != null && !"".equals(param.getAction())) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " uc_name like "+ "'%"+param.getAction()+"%'";
			placeAND = true;
		}
		if (param.getIpAddress() != null && !"".equals(param.getIpAddress())) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " remoteip like "+ "'%"+param.getIpAddress()+"%'";
			placeAND = true;
		}
		if (param.getMessage() != null && !"".equals(param.getMessage())) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " uc_desc like "+ "'%"+param.getMessage()+"%'";
			queryString += " OR uc_msg like "+ "'%"+param.getMessage()+"%'";
			queryString += " OR uc_id like "+ "'%"+param.getMessage()+"%'";
			queryString += " OR uc_no like "+ "'%"+param.getMessage()+"%'";
			placeAND = true;
		}
		if (param.getUser() != null && !"".equals(param.getUser())) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " username like "+ "'%"+param.getUser()+"%'";
			placeAND = true;
		}
		
		
		
		if ( (param.getFromDate() != null && !"".equals(param.getFromDate())) && (param.getToDate() != null && !"".equals(param.getToDate())) ) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " date BETWEEN " + "'" + param.getFromDate() + "' AND '" + param.getToDate() + "'" ;
			placeAND = true;
		}
		if ( (param.getFromDate() != null && !"".equals(param.getFromDate())) && (param.getToDate() == null || "".equals(param.getToDate())) ) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " date >= " + "'" + param.getFromDate() + "'";
			placeAND = true;
		}
		if ( (param.getFromDate() == null || "".equals(param.getFromDate())) && (param.getToDate() != null && !"".equals(param.getToDate())) ) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " date <= " + "'" + param.getToDate() + "'" ;
			placeAND = true;
		}

		
		if ( (param.getFromTime() != null && !"".equals(param.getFromTime())) && (param.getToTime() != null && !"".equals(param.getToTime())) ) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " time BETWEEN "+ "'"+param.getFromTime()+"' AND '" + param.getToTime() + "'" ;
			placeAND = true;
		}
		if ( (param.getFromTime() != null && !"".equals(param.getFromTime())) && (param.getToTime() == null || "".equals(param.getToTime())) ) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " time >= " + "'" + param.getFromTime() + "'";
			placeAND = true;
		}
		if ( (param.getFromTime() == null || "".equals(param.getFromTime())) && (param.getToTime() != null && !"".equals(param.getToTime())) ) {
			if (placeAND) queryString += " AND "; 
			else queryString += " WHERE ";
			queryString += " time <= " + "'" + param.getToTime() + "'";
			placeAND = true;
		}
		
		
		if (!is4count) {
			queryString += " ORDER BY id DESC ";
		}
		if (pageSize != 0 && reqPage != 0) {
			queryString += "LIMIT " + (reqPage - 1) * pageSize + " , " + pageSize; 
		}
		return queryString;
	}

	private String getString(String strInput) {
		String strOutput = NVL.getString(strInput,"-");
		if ("null".equals(strOutput)) {
			strOutput = "-";
		}
		return strOutput;
	}
	
	public void log(NIOLogENT logENT) {
		try {
			Statement stmt = getConnection().createStatement();
			String str = "INSERT INTO aip_niolog";
			str += "(date,time,username,remoteip,uc_name,uc_id,uc_no,uc_op,uc_oldcriticaldata,uc_desc,uc_msg,uc_res,uc_action_reqcode_forward,puc_name,puc_id,puc_no)";
			str += "VALUES";
			str += "(";
			str += "'"+getDate()+"'," +
					"'"+getTime()+"'," +
					"'"+logENT.getUsername()+"'," +
					"'"+logENT.getRemoteip()+"'," +
					"'"+logENT.getUc_name()+"'," +
					"'"+logENT.getUc_id()+"'," +
					"'"+logENT.getUc_no()+"'," +
					"'"+logENT.getUc_op()+"'," +
					"'"+logENT.getUc_oldcriticaldata()+"'," +
					"'"+logENT.getUc_desc()+"'," +
					"\""+logENT.getUc_msg()+"\"," +
					"'"+logENT.getUc_res()+"'," +
					"'"+logENT.getUc_action_reqcode_forward()+"'," +
					"'"+logENT.getPuc_name()+"'," +
					"'"+logENT.getPuc_id()+"'," +
					"'"+logENT.getPuc_no()+"'";
			str +=	")";
			//stmt.execute(str);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getTime() {
		Calendar calendar = new GregorianCalendar();
		String time = "";
		if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
			time += "0" + calendar.get(Calendar.HOUR_OF_DAY) + ":";
		} else {
			time += calendar.get(Calendar.HOUR_OF_DAY) + ":";
		}
		if (calendar.get(Calendar.MINUTE) < 10) {
			time += "0" + calendar.get(Calendar.MINUTE) + ":";
		} else {
			time += calendar.get(Calendar.MINUTE) + ":";
		}
		if (calendar.get(Calendar.SECOND) < 10) {
			time += "0" + calendar.get(Calendar.SECOND);
		} else {
			time += calendar.get(Calendar.SECOND);
		}
		return time;
	}

	private String getDate() {
		String date = DateConvert.getTodayJalali();
		return date;
	}
	
	public static String printLog(Object obj){
		StringBuffer sb = new StringBuffer();
		printLog(obj, sb);
		return sb.toString();
	}
	
	private static void printLog(Object obj, StringBuffer sb) {
		if (obj == null) {
			sb.append("{null}");
			return;
		}
		Class cls = obj.getClass();
		ArrayList<Field> flds = new ArrayList<Field>();
		Field[] fs;

		fs = cls.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			flds.add(fs[i]);
		}
		
		int p = 0;
		sb.append(obj.getClass().getSimpleName() + ":<br>");
		for (int i = 0; i < flds.size(); i++) {
			Object value = "";
			try {
				value = invoke(obj, flds.get(i).getName());
			} catch (Exception e) {
				value = "(err)";
			}
			if (value == null) {
			} else if (value instanceof List) {
				List l = (List) value;
				sb.append(flds.get(i).getName() + "<br>");
				for (int j = 0; j < l.size(); j++) {
					printLog(l.get(j), sb);
				}
			} else if (value instanceof Object[]) {
			} else if (value instanceof Integer || value instanceof Double || value instanceof Long || value instanceof Boolean) {
				p++;
				sb.append(flds.get(i).getName() + " = " + value + ";&nbsp;&nbsp;&nbsp;");
				if (p % 5 == 0) {
					sb.append("<br>");
				}
			} else if (!(value instanceof String) && value instanceof Object) {
				sb.append("<br>");
				sb.append("<br>");
				sb.append("<br>");
				printLog(value, sb);
				sb.append("<br>");
				sb.append("<br>");
				sb.append("<br>");
			} else {
				p++;
				sb.append(flds.get(i).getName() + " = " + value + ";&nbsp;&nbsp;&nbsp;");
				if (p % 5 == 0) {
					sb.append("<br>");
				}
			}
		}
		sb.append("<br>");
	}
	
	public static Object invoke(Object obj,String field) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String field2invoke = field.substring(0, 1).toUpperCase() + field.substring(1);
		Object v = null;
		try {
			v = obj.getClass().getMethod("get"+field2invoke,null ).invoke(obj, null);
		} catch (Exception e) {
			v = obj.getClass().getMethod("is"+field2invoke,null ).invoke(obj, null);
		}
		return v;
	}
	
	public static void rebuildSessionFactory() {
		try {
			configuration.configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}
*/
}
