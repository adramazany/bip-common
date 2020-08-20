package aip.util.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import aip.util.AIPException;
import aip.util.AIPPrintParam;
import aip.util.AIPUtil;
import aip.util.DateConvert;
import aip.util.NVL;
import aip.db.AIPDBUtil;

public class AIPLog {
	private final static String dataSourceName="jdbc/aiplogds";

	public final static String LEVEL_ERROR="ERROR";
	public final static String LEVEL_WARN="WARNING";
	public final static String LEVEL_INFO="INFO";
	
	public final static String OP_LIST="لیست";
	public final static String OP_VIEW="نمایش";
	public final static String OP_INSERT="ایجاد";
	public final static String OP_UPDATE="تغییر";
	public final static String OP_DELETE="حذف";
	
	private final static String createLogTable =" CREATE TABLE aiplog (" +
			" id int AUTO_INCREMENT," +
			" date char(10) NOT NULL," +
			" time char(8) NOT NULL," +
			" username varchar(50) NOT NULL," +
			" remoteip varchar(15) NOT NULL," +
			" uc_name varchar(50) NOT NULL," +
			" uc_level varchar(10)," +
			" uc_id varchar(20)," +
			" uc_no varchar(20)," +
			" uc_op varchar(50)," +
			" uc_desc varchar(4000)," +
			" PRIMARY KEY (id)" +
			")";
	
	//void log(NIOLogENT logENT);
	static Connection conn=null;
	//static String sqlLogInsert = "INSERT INTO aiplog (date,time,username,remoteip,uc_name,uc_id,uc_no,uc_op,uc_oldcriticaldata,uc_desc,uc_msg,uc_res,uc_action_reqcode_forward,puc_name,puc_id,puc_no) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	static String sqlLogInsert = "INSERT INTO aiplog (date,time,username,remoteip,uc_name,uc_level,uc_id,uc_no,uc_op,uc_desc) values (?,?,?,?,?,?,?,?,?,?)";
	/*
	 * ? 1,2=fromDate
	 * ? 3,4=toDate
	 * ? 5,6=fromHour
	 * ? 7,8=toHour
	 * ? 9,10=username
	 * ? 11,12=uc_name//,puc_name
	 * ? 13,14=uc_level
	 * ? 15,16=uc_no//,puc_no
	 * ? 17,18=uc_op
	 * ? 19,20=uc_desc //uc_oldcriticaldata,uc_desc,uc_msg,uc_res,uc_action_reqcode_forward
	 */
//	static String sqlLogCommonWhere =  
//		" (? is null or date >= ?) "+
//        " AND (? is null or date <= ?) "+
//        " AND (? is null or time >= ?) "+
//        " AND (? is null or time <= ?) "+
//        " AND (? is null or username like ?) "+ 
//        " AND (? is null or uc_name like ? or puc_name like ?) "+ 
//        " AND (? is null or uc_no like ? or puc_no like ?) "+
//        " AND (? is null or uc_op like ?) "+
//        " AND (? is null or uc_name like ?) "+
//        " AND (? is null or uc_oldcriticaldata like ? or uc_desc like ? or uc_msg like ? or uc_res like ? or uc_action_reqcode_forward like ?)"; 
	static String sqlLogCommonWhere =  
		" (? is null or date >= ?) "+
        " AND (? is null or date <= ?) "+
        " AND (? is null or time >= ?) "+
        " AND (? is null or time <= ?) "+
        " AND (? is null or username like ?) "+ 
        " AND (? is null or uc_name like ?) "+ 
        " AND (? is null or uc_level like ?) "+
        " AND (? is null or uc_no like ?) "+
        " AND (? is null or uc_op like ?) "+
        " AND (? is null or uc_desc like ?) ";
	static String sqlLogCount = "SELECT count(*) FROM aiplog WHERE "+ sqlLogCommonWhere;
	static String sqlLogList = "SELECT * FROM aiplog WHERE "+sqlLogCommonWhere+" ORDER BY date desc,time desc limit ?,?";  
	
	static PreparedStatement psLogInsert = null;
	static PreparedStatement psLogCount = null;
	static PreparedStatement psLogList = null;
	
	private static boolean is_check_tables_exist=false;
	
	private static Connection getConnection() throws Exception{
		if(conn==null || conn.isClosed()){
			try {
				conn = AIPDBUtil.getDataSourceConnection(dataSourceName);
				return conn;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Properties logProps = new Properties();
			if(new File("aiplog.properties").exists()){
				FileInputStream logPropsFile = new FileInputStream("aiplog.properties");
		        logProps.load(logPropsFile);
		        logPropsFile.close();
			}else{
		        logProps.setProperty("driver", "com.mysql.jdbc.Driver" );
		        logProps.setProperty("url", "jdbc:mysql://localhost:3306/aiplog?characterEncoding=utf8");
		        logProps.setProperty("user", "root");
		        logProps.setProperty("password",  "aippia");
		        
		        logProps.store(new FileOutputStream("aiplog.properties"), "درگاه هوش مصنوعی \n Artifitial Inteligence Portal Co \n www.aip-co.com");
			}

	        String driver = logProps.getProperty("driver", "com.mysql.jdbc.Driver");
	        String url = logProps.getProperty("url", "jdbc:mysql://localhost:3306/aiplog?characterEncoding=utf8");
	        String user = logProps.getProperty("user", "root");
	        String password = logProps.getProperty("password", "aippia");

	        
	        Class.forName(driver);
	        conn = DriverManager.getConnection(url,user,password);
		}
		return conn;
	}
	private static PreparedStatement getStatementInsert() throws Exception{
		if(psLogInsert==null){
			psLogInsert = getConnection().prepareStatement(sqlLogInsert); 
		}
		return psLogInsert;
	}
	private static PreparedStatement getStatementCount() throws Exception{
		if(psLogCount==null){
			psLogCount = getConnection().prepareStatement(sqlLogCount); 
		}
		return psLogCount;
	}
	private static PreparedStatement getStatementList() throws Exception{
		if(psLogList==null){
			psLogList = getConnection().prepareStatement(sqlLogList); 
		}
		return psLogList;
	}
	
	public static void log(AIPLogENT logENT){
		
		try {
			if(is_check_tables_exist==false){
				checkTablesExistAndCreate(getConnection());
				is_check_tables_exist=true;
			}
			
			
			if(NVL.isEmpty(logENT.date)){
				logENT.date=DateConvert.getTodayJalali();
			}
			
			if(NVL.isEmpty(logENT.time)){
				logENT.time=NVL.getNowTime();
			}
			

			PreparedStatement ps = getStatementInsert();
			
			ps.setString(1,NVL.getString( logENT.getDate(),DateConvert.getTodayJalali() ));
			ps.setString(2, NVL.getString( logENT.getTime(),NVL.getNowTime() ) );
			ps.setString(3,logENT.getUsername());
			ps.setString(4,logENT.getRemoteip());
			ps.setString(5,logENT.getUc_name());
			ps.setString(6,NVL.getString( logENT.getUc_level() , LEVEL_INFO ) );
			ps.setString(7,NVL.getStringNull(logENT.getUc_id()));
			ps.setString(8,NVL.getStringNull(logENT.getUc_no()));
			ps.setString(9,NVL.getStringNull(logENT.getUc_op()));
//			ps.setString(9,logENT.getUc_oldcriticaldata());
			ps.setString(10, NVL.getStringNull(logENT.getUc_desc()));
//			ps.setString(11,logENT.getUc_msg());
//			ps.setString(12,logENT.getUc_res());
//			ps.setString(13,logENT.getUc_action_reqcode_forward());
//			ps.setString(14,logENT.getPuc_name());
//			ps.setString(15,logENT.getPuc_id());
//			ps.setString(16,logENT.getPuc_no());
			
			ps.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void checkTablesExistAndCreate(Connection cn)throws Exception {
		ResultSet rs; 
		rs = cn.getMetaData().getTables(null, null, "aiplog", new String[]{"TABLE"});
		boolean is_exist_LOGSEARCHLOG = rs.next();
		rs.close();
		if(!is_exist_LOGSEARCHLOG){
//			boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle")>-1;
//			if(isoracle){
//				AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHLOG (id number primary key,f_logsearchsentenceid number,logdate char(10),time char(8),username varchar2(50),remoteip char(25),remotehost varchar2(50),remoteport int,authtype varchar2(20),sessionid varchar2(50),userprincipal varchar2(100))");
//				AIPDBUtil.executeUpdate(cn, "create sequence "+seq_logsearchlog);
//			}else{
				AIPDBUtil.executeUpdate(cn, createLogTable);
//			}
		}
		
	}
	public synchronized static AIPLogLST logList(AIPLogParam param){
		AIPLogLST lst= new AIPLogLST();
		try{
			String fromDate = NVL.getStringNull(param.getFromDate()); 
			String toDate = NVL.getStringNull(param.getToDate()); 
			String fromHour = NVL.getStringNull(param.getFromHour()); 
			String toHour = NVL.getStringNull(param.getToHour()); 
			String username = NVL.getStringNull(param.getUsername()); 
			String uc_name = NVL.getStringNull(param.getUc_name()); 
			String uc_level = NVL.getStringNull(param.getUc_level()); 
			String uc_no = NVL.getStringNull(param.getUc_no()); 
			String uc_op = NVL.getStringNull(param.getUc_op()); 
			String uc_desc = NVL.getStringNull(param.getUc_desc());
			
			username = username!=null?"%"+username+"%":null;
			uc_name = uc_name!=null?"%"+uc_name+"%":null;
			uc_level = uc_level!=null?"%"+uc_level+"%":null;
			uc_no = uc_no!=null?"%"+uc_no+"%":null;
			uc_op = uc_op!=null?"%"+uc_op+"%":null;
			uc_desc = uc_desc!=null?"%"+uc_desc+"%":null;

			PreparedStatement psCount = getStatementCount();
				psCount.setString(1, fromDate);//is null
				psCount.setString(2, fromDate);//>= fromDate
				psCount.setString(3, toDate);//is null
				psCount.setString(4, toDate);//<= toDate
				psCount.setString(5, fromHour);//is null
				psCount.setString(6, fromHour);//>=fromHour
				psCount.setString(7, toHour);//is null
				psCount.setString(8, toHour);//<=toHour
				psCount.setString(9, username);//is null
				psCount.setString(10, username);//like username
				psCount.setString(11, uc_name);//is null
				psCount.setString(12, uc_name);//like uc_name
				//psCount.setString(13, uc_name);//like puc_name
				psCount.setString(13, uc_level);//is null
				psCount.setString(14, uc_level);//like uc_name
				psCount.setString(15, uc_no);//is null
				psCount.setString(16, uc_no);//like uc_no
				//psCount.setString(16, uc_no);//like puc_no
				psCount.setString(17, uc_op);//is null
				psCount.setString(18, uc_op);//like uc_op
				psCount.setString(19, uc_desc);//is null
				psCount.setString(20, uc_desc);//like uc_desc 
//				psCount.setString(21, etc);//like uc_oldcriticaldata
//				psCount.setString(22, etc);//like uc_msg
//				psCount.setString(23, etc);//like uc_res
//				psCount.setString(24, etc);//like uc_action_reqcode_forward

			ResultSet rsCount = psCount.executeQuery();
			if(rsCount.next()){
				lst.setTotalItems( NVL.getLng( rsCount.getObject(1) ) );
			}
			rsCount.close();
			
			
			lst.setParam(param);
			
			PreparedStatement psList = getStatementList();
			psList.setString(1, fromDate);//is null
			psList.setString(2, fromDate);//>= fromDate
			psList.setString(3, toDate);//is null
			psList.setString(4, toDate);//<= toDate
			psList.setString(5, fromHour);//is null
			psList.setString(6, fromHour);//>=fromHour
			psList.setString(7, toHour);//is null
			psList.setString(8, toHour);//<=toHour
			psList.setString(9, username);//is null
			psList.setString(10, username);//like username
			psList.setString(11, uc_name);//is null
			psList.setString(12, uc_name);//like uc_name
			//psList.setString(13, uc_name);//like puc_name
			psList.setString(13, uc_level);//is null
			psList.setString(14, uc_level);//like uc_name
			psList.setString(15, uc_no);//is null
			psList.setString(16, uc_no);//like uc_no
			//psList.setString(16, uc_no);//like puc_no
			psList.setString(17, uc_op);//is null
			psList.setString(18, uc_op);//like uc_op
			psList.setString(19, uc_desc);//is null
			psList.setString(20, uc_desc);//like uc_desc
//			psList.setString(20, etc);//like uc_oldcriticaldata
//			psList.setString(22, etc);//like uc_msg
//			psList.setString(23, etc);//like uc_res
//			psList.setString(24, etc);//like uc_action_reqcode_forward

			List<AIPLogENT> dtos = new ArrayList<AIPLogENT>();
			if(param.getPrintParam().getPrintRange()==AIPPrintParam.RANGE_PAGE_CURRENT){
				psList.setLong(21, lst.getFirstRow());//limit firstrow
				psList.setLong(22, lst.getLastRow());//limit lastrow
				ResultSet rsList = psList.executeQuery();
				while(rsList.next()){
					dtos.add(  getLogENTofRS(rsList) );
				}
				rsList.close();
			}else if(param.getPrintParam().getPrintRange()==AIPPrintParam.RANGE_PAGE_ALL){
				psList.setLong(21, 1);//limit firstrow
				psList.setLong(22, lst.getTotalItems());//limit lastrow
				ResultSet rsList = psList.executeQuery();
				while(rsList.next()){
					dtos.add(  getLogENTofRS(rsList) );
				}
				rsList.close();
			}else{
				long[][] startsAndEnds=param.getPrintParam().getRequestStartsAndEnds(lst.getPageSize(), lst.getTotalItems());
				for(int i=0;i<startsAndEnds.length;i++){
					psList.setLong(21, startsAndEnds[i][0]);//limit firstrow
					psList.setLong(22, startsAndEnds[i][1]);//limit lastrow
					ResultSet rsList = psList.executeQuery();
					while(rsList.next()){
						dtos.add(  getLogENTofRS(rsList) );
					}
					rsList.close();
				}
			}
			
			lst.setRows( dtos );

			lst.setParamString( getParamString(param) );
		}
		catch(Exception e){
			e.printStackTrace();
			//throw new AIPException(e);
		}
		
			
		return lst;
	}
	private static String getParamString(AIPLogParam param) {
		StringBuffer sb = new StringBuffer();
		
		sb.append( getParamStringDateHour("از تاریخ: ",param.getFromDate(),param.getFromHour()) );
		sb.append( getParamStringDateHour("تا تاریخ: ",param.getToDate(),param.getToHour()) );
		sb.append( getParamStringOther("کاربر",param.getUsername()) );
		sb.append( getParamStringOther("مورد کاربرد",param.getUc_name()) );
		sb.append( getParamStringOther("سطح",param.getUc_level()) );
		sb.append( getParamStringOther("شماره",param.getUc_no()) );
		sb.append( getParamStringOther("متد",param.getUc_op()) );
		sb.append( getParamStringOther("شرح",param.getUc_desc()) );
		
		return sb.toString();
	}
	private static String getParamStringDateHour(String title,String fromDate, String fromHour) {
		String st="";
		if( !NVL.isEmpty(fromDate) ){
			st = title;
			st += fromDate;
			if( !NVL.isEmpty(fromHour) ){
				st += " " + fromHour;
			}
			st += ",";
		}
		return st;
	}
	public static String getParamStringOther(String name,String value) {
		String st = "";
		if( !NVL.isEmpty(value) ){
			st+=name+" : "+value+",";
		}
		return st;
	}
	private synchronized static AIPLogENT getLogENTofRS(ResultSet rsList) throws SQLException {
		AIPLogENT ent = new AIPLogENT();
		
		ent.setId(rsList.getInt("id"));
		ent.setDate(rsList.getString("date"));
		ent.setTime(rsList.getString("time"));
		ent.setUsername(rsList.getString("username"));
		ent.setRemoteip(rsList.getString("remoteip"));
		ent.setUc_name(rsList.getString("uc_name"));
		ent.setUc_level(rsList.getString("uc_level"));
		ent.setUc_id(rsList.getString("uc_id"));
		ent.setUc_no(rsList.getString("uc_no"));
		ent.setUc_op(rsList.getString("uc_op"));
		//ent.setUc_oldcriticaldata(rsList.getString("uc_oldcriticaldata"));
		ent.setUc_desc(rsList.getString("uc_desc"));
		//ent.setUc_msg(rsList.getString("uc_msg"));
		//ent.setUc_res(rsList.getString("uc_res"));
		//ent.setUc_action_reqcode_forward(rsList.getString("uc_action_reqcode_forward"));
		//ent.setPuc_name(rsList.getString("puc_name"));
		//ent.setPuc_id(rsList.getString("puc_id"));
		//ent.setPuc_no(rsList.getString("puc_no"));

		return ent;
	}

	public static String extractChangedCriticalData(Object newObj, Object oldObj, String[] criticalFields) {
		String criticalData = "";
		for (int i = 0 ; i < criticalFields.length ; i++) {
			try {
				Object newFieldData = AIPUtil.invokeMulti(newObj, criticalFields[i]);
				Object oldFieldData = AIPUtil.invokeMulti(oldObj, criticalFields[i]);
				if (!newFieldData.equals(oldFieldData)) {
					criticalData += "("+criticalFields[i] + " Prev: " + oldFieldData + " New: " + newFieldData + ")";   
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return criticalData;
	}
	public static Exception logThrow(Exception ex, AIPLogENT logENT) {
		logENT.addUc_error(ex);
		log(logENT);
		return ex;
	}
	public static AIPException logThrow(AIPException ex, AIPLogENT logENT) {
		logENT.addUc_error(ex);
		log(logENT);
		return ex;
	}


}
