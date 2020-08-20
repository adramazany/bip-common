package bip.etl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import aip.db.AIPDBUtil;
import aip.db.AIPTableModel;
import aip.util.AIPConfiguration;
import aip.util.AIPUtil;
import aip.util.DateConvert;
import aip.util.NVL;

public abstract class ETLAbstract {
	public static String NULL_VALUE="-1";
	
	Properties queries = null;
	String xmlResourceName = this.getClass().getSimpleName()+getETLFileDatabaseRelatedSuffix()+".xml";
	//Collection<String> existIds;
	ProcessParam processParam;

	public abstract void update(ProcessETLListener processETL)throws Exception;
	public abstract void process(ProcessETLListener processETL)throws Exception;
	public abstract void fillSQLParameter(StringBuffer sql, ProcessETLListener processETL, Connection cnsrc, Connection cndest, AIPTableModel rs)throws Exception;

	public void update(ProcessETLListener processETL,ProcessLog processLog)throws Exception{
		String query_count="query-count";
		String query_etl="query-etl";
		/*
		 * codemelli
		 */
/*		if(!NVL.isEmpty(processETL.getProcessParam().getCodemelli())){ 
			if(!NVL.isEmpty(getQuery("query-count-codemelli", "", "")) ){
				query_count = "query-count-codemelli";
		}
		if(!NVL.isEmpty(getQuery("query-etl-codemelli", "", "")) ){
			query_etl = "query-etl-codemelli";
		}
	}
*/		
		/*
		 * tarikh
		 */
/*		else if(!NVL.isEmpty(processETL.getProcessParam().getTarikhaz()) && !NVL.isEmpty(processETL.getProcessParam().getTarikhta()) ){ 
			if(!NVL.isEmpty(getQuery("query-count-tarikh", "", "")) ){
				query_count = "query-count-tarikh";
			}
			if(!NVL.isEmpty(getQuery("query-etl-tarikh", "", "")) ){
				query_etl = "query-etl-tarikh";
			}
		}
*/
		if(!NVL.isEmpty(processETL.getProcessParam().getOtherQueryCount())
		&& !NVL.isEmpty(processETL.getProcessParam().getOtherQueryEtl())
				){ 
			if(!NVL.isEmpty(getQuery(processETL.getProcessParam().getOtherQueryCount(), "", "")) ){
				query_count = processETL.getProcessParam().getOtherQueryCount();
			}
			if(!NVL.isEmpty(getQuery(processETL.getProcessParam().getOtherQueryEtl(), "", "")) ){
				query_etl = processETL.getProcessParam().getOtherQueryEtl();
			}
		}
		/*
		 * update
		 */
		update(processETL,processLog, "query-maxid",query_etl,query_count,"sql-insert","sql-update");
		
	}
	
	public void update(ProcessETLListener processETL,ProcessLog processLog, String query_maxid_name, String query_etl_name, String query_count_name
			, String sql_insert_name, String sql_update_name)throws Exception{
		
		Connection cnsrc =null;
		Connection cndest =null;
		AIPTableModel rs = null;
		StringBuffer sqlInsert=null;
		StringBuffer sqlUpdate=null;
		
		processParam = processETL.getProcessParam();
		try {

			cnsrc = AIPDBUtil.getDataSourceConnection(getEtlSourceDS());
			cndest = AIPDBUtil.getDataSourceConnection(getEtlDestinationDS());

			/**
			 * preStartETL
			 */
			preStartETL(processETL,cnsrc,cndest);
			
			
			
			String maxid = "";
			if(processETL.isFullProcess()){
				//loadExistIds(cndest,query_existids_name);
			}else{
				maxid = NVL.getString( AIPDBUtil.executeScalar(cndest, getQuery(query_maxid_name, null, null)) );
			}
			System.out.println("maxid="+maxid);
			
			String queryCount = getQuery(query_count_name,":maxid", ""+maxid);
			String query = getQuery(query_etl_name,":maxid",""+maxid);//getQueryETL(""+maxid);
			String srcIdField = getQuery("query-onebyone-id",null,null);

			if(!NVL.isEmpty(processParam.getTableId())){
				maxid=processParam.getTableId();

				queryCount = getQuery(query_count_name+"-tableid",":maxid", ""+maxid);
				query = getQuery(query_etl_name+"-tableid",":maxid",""+maxid);//getQueryETL(""+maxid);
				
				if(NVL.isEmpty(queryCount)){
					queryCount = getQuery(query_count_name,">:maxid_int", "="+maxid);
				}
				if(NVL.isEmpty(query)){
					query = getQuery(query_etl_name,">:maxid_int", "="+maxid);
				}
				
/*				queryCount = AIPUtil.replaceAllString(queryCount,">':maxid'", "='"+maxid+"'");
				query = AIPUtil.replaceAllString(query,">':maxid'", "='"+maxid+"'");
				
				queryCount = AIPUtil.replaceAllString(queryCount,">:maxid", "="+maxid);
				query = AIPUtil.replaceAllString(query,">:maxid", "="+maxid);

				queryCount = AIPUtil.replaceAllString(queryCount,"DB_COMMIT_DATE>TO_DATE(NVL(':maxid','0001-01-01'),'YYYY-MM-DD HH24:MI:SS')", "person_nin="+maxid);
				query = AIPUtil.replaceAllString(query,"DB_COMMIT_DATE>TO_DATE(NVL(':maxid','0001-01-01'),'YYYY-MM-DD HH24:MI:SS')", "person_nin="+maxid);
*/			}
			
			processETL.setQuery(query);
			int totalcount = NVL.getInt( AIPDBUtil.executeScalar(cnsrc, queryCount ) ); //getQueryETLCount(""+maxid) ) );
			processETL.setTotalcount(totalcount);
			processLog.logStartProcess(cndest,maxid,totalcount,processETL.getProcessParam());
			
			
			//2
			rs = AIPDBUtil.executeQuery2Table(cnsrc, query);

			String query_onebyone_id = getQuery("query-onebyone-id", "", "");
			int batch_len = NVL.getInt( getQuery("query-etl-batch-len", "", "") , 1);
			String id_prefix = NVL.getString(getQuery("query-etl-id-prefix", "", ""));
			String id_suffix = NVL.getString(getQuery("query-etl-id-suffix", "", ""));
			
			List<String> batch_query_oneid = new ArrayList<String>(); 
			//List<String> tableIds = new ArrayList<String>(); 
			
			int counter=1;
			int errorCounter=0;
			processETL.setProcessStartTime(new Date());
			
			boolean hasParamFilter=false;
			if(processParam!=null 
					&& (processParam.getOtherParams().size()>0
						|| (!NVL.isEmpty(processParam.getTarikhaz()) && !NVL.isEmpty(processParam.getTarikhta()) )
						)
				){
				hasParamFilter=true;
			}
					
			
			
			//get full rows from src 
			if(NVL.isEmpty(query_onebyone_id)){
				while(rs.next()){
					sqlInsert = new StringBuffer( getQuery(sql_insert_name, null, null) );
					sqlUpdate = new StringBuffer( getQuery(sql_update_name, null, null) );
					
					try {
						fillSQLParameter(sqlInsert,processETL,cnsrc,cndest,rs);
						fillSQLParameter(sqlUpdate,processETL,cnsrc,cndest,rs);

						List<String> tableIds = new ArrayList<String>();
						int sqlErrorCounter = executeSQL(batch_len,counter,rs.getString(srcIdField),cndest,sqlInsert.toString(),batch_query_oneid,totalcount,tableIds,processLog);
						errorCounter += sqlErrorCounter;
						
						processETL.setErrorCounter(errorCounter);
						
					} catch (Exception e) {
						e.printStackTrace();
						
						processETL.setErrorCounter(++errorCounter);
						processLog.logException(e, cndest, processParam, rs.getString(srcIdField));
					}

					
					processETL.setCounter(counter++);
				}//while
				
			}else{//get onebyone rows from src
				StringBuilder ids=new StringBuilder();
//				Hashtable<String, StringBuffer> htSQL = new Hashtable<String, StringBuffer>();
//				SortedMap<String, StringBuffer> htSQL = new SortedMap<String, StringBuffer>();
				LinkedHashMap<String, StringBuffer> htSQL = new LinkedHashMap<String, StringBuffer>();
				
				while(rs.next()){
					sqlInsert = new StringBuffer( getQuery(sql_insert_name, null, null) );
					sqlUpdate = new StringBuffer( getQuery(sql_update_name, null, null) );

					if(batch_len>1){
						htSQL.put(rs.getString(query_onebyone_id), sqlInsert);
						ids.append(id_prefix);
						ids.append(rs.getString(query_onebyone_id));
						ids.append(id_suffix);
						ids.append(",");
						
						//tableIds.add(rs.getString(query_onebyone_id));
						
						if(counter%batch_len==0 || counter==totalcount){
							//System.out.println("ETLAbstract.update():last-query-etl-oneid:"+query_onebyone+"="+rs.getString(query_onebyone)+",counter="+counter);
							ids.delete(ids.length()-1, ids.length());
							String query_oneid = getQuery("query-etl-oneid", ":id", ids.toString());
							ids.delete(0, ids.length());

							AIPTableModel rs_onebyone = AIPDBUtil.executeQuery2Table(cnsrc, query_oneid);

							String[] sqls = new String[htSQL.size()];
							String[] tableIds2 = new String[htSQL.size()];
							int i=0;
							while(rs_onebyone.next()){
								sqlInsert = htSQL.get(rs_onebyone.getString(query_onebyone_id));
								
								/*
								 * manually cause exception
								 */
//								if("4568649031".equalsIgnoreCase(rs_onebyone.getString("PERSON_NIN"))){
//									//936816491
//									AIPUtil.replaceAllString(sql,":f_tarikh_fot", NVL.getIntegerNull(1) );
//								}
								
								try {
									fillSQLParameter(sqlInsert,processETL,cnsrc,cndest,rs_onebyone);
									tableIds2[i]=rs_onebyone.getString(query_onebyone_id);
									sqls[i++]=sqlInsert.toString();
								} catch (Exception e) {
									e.printStackTrace();
									
									processETL.setErrorCounter(++errorCounter);
									processLog.logException(e, cndest, processParam, rs_onebyone.getString(query_onebyone_id));
								}
							}
							rs_onebyone.close();

//							int i=0;
//							for (Iterator iterator = htSQL.values().iterator(); iterator.hasNext();) {
//								sqls[i++] = ((StringBuffer) iterator.next()).toString();
//								
//							}
							sqlInsert.delete(0, sqlInsert.length()-1);
							int batchErrorCounter = executeBatch(cndest,sqls,false
									,tableIds2,":tableid"
									,processLog.getLogExceptionSql(processParam,processLog.LOG_ERROR).toString(),":logdesc");
							//,tableIds.toArray(new String[0]),":tableid"
							errorCounter+=batchErrorCounter;
							processETL.setErrorCounter(errorCounter);

							htSQL.clear();
							//tableIds.clear();
							
							//executeBatch(cndest,batch_query_oneid.toArray(new String[0]),false);
							System.out.println("ETLAbstract.update()-batch:"+query_onebyone_id+",totalcount="+totalcount+",batch_len="+batch_len+",counter="+counter+",remaintime="+processETL.estimateRemainTime(new Date()));
							
						}
					}else{
						System.out.println("ETLAbstract.update():query-etl-oneid:"+query_onebyone_id+"="+rs.getString(query_onebyone_id));
						String query_oneid = getQuery("query-etl-oneid", ":id", rs.getString(query_onebyone_id));
						AIPTableModel rs_onebyone = AIPDBUtil.executeQuery2Table(cnsrc, query_oneid);
						rs_onebyone.next();

						System.out.println("id="+rs.getString(srcIdField));
						try {
							fillSQLParameter(sqlInsert,processETL,cnsrc,cndest,rs_onebyone);
							AIPDBUtil.executeUpdate(cndest,sqlInsert.toString());
						} catch (Exception e) {
							e.printStackTrace();

							processETL.setErrorCounter(++errorCounter);
							processLog.logException(e,cndest, processParam,rs.getString(srcIdField));
						}
					}
					
					processETL.setCounter(counter++);
				}
			}
			if(counter>0)counter--;
			System.out.println("counter="+counter);
			
			if(!NVL.isEmpty(processParam.getTableId()) && errorCounter==0){
				processLog.logExceptionResolved(cndest, processParam, processParam.getTableId());
			}
			

			/**
			 * postEndETL
			 */
			postEndETL(processETL,cnsrc,cndest);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			processLog.logException(e,cndest, processParam,"");
			processETL.setException(e);
			processETL.setExceptionSQL(sqlInsert.toString());
			throw e;
		}finally{
			try {
				if(rs!=null)rs.close();
				if(cnsrc!=null)cnsrc.close();
				if(cndest!=null)cndest.close();
				rs=null;cnsrc=null;cndest=null;
			} catch (Exception e2) {e2.printStackTrace();}
		}
	}


	protected void preStartETL(ProcessETLListener processETL, Connection cnsrc, Connection cndest) {
		executeSQLIgnoreError(cndest, getQuery("preStartETL", null, null));
	}
	protected void postEndETL(ProcessETLListener processETL, Connection cnsrc, Connection cndest) {
		executeSQLIgnoreError(cndest, getQuery("postEndETL", null, null));
	}
	private void executeSQLIgnoreError(Connection cn,String sql){
		Statement stmt=null;
		try {
			if(!NVL.isEmpty(sql)){
				stmt = cn.createStatement();
				stmt.execute(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)stmt.close();
			}catch(Exception e){}
		}
	}
	

	private int executeSQL(int batch_len, int counter, String srcId, Connection cndest, String sql,List<String> batch_query_oneid, int totalcount,List<String> tableIds, ProcessLog processLog) throws Exception {
		int errorCounter=0;
		batch_query_oneid.add(sql);
		tableIds.add(srcId);
		if(batch_len>1){
			if(counter%batch_len==0 || counter==totalcount){
				System.out.println("batch-last-id="+srcId);
				int batchErrorCounter = executeBatch(cndest,batch_query_oneid.toArray(new String[0]),false
						,tableIds.toArray(new String[0]),":tableid"
						,processLog.getLogExceptionSql(processParam,processLog.LOG_ERROR).toString(),":logdesc");
				errorCounter += batchErrorCounter;
				batch_query_oneid.clear();
				tableIds.clear();
			}
		}else{
			//System.out.println(sql);
			System.out.println("id="+srcId);
			try {
				AIPDBUtil.executeUpdate(cndest,sql);
			} catch (Exception e) {
				e.printStackTrace();
				errorCounter++;
				 processLog.logException(e,cndest, processParam,srcId);
				
			}
		}
		return errorCounter;
	}

	
	
	public String getXmlResourceName() {
		return xmlResourceName;
	}
	public void setXmlResourceName(String xmlResourceName) {
		this.xmlResourceName = xmlResourceName;
	}

	private int executeBatch(Connection cn, String[] sql,boolean rollbackAll,String tableIds[],String logTableIdName,String logExceptionSql,String logDescName) throws Exception {
		int errorCounter=0;
		Statement stmt = null;
		boolean autoCommit = cn.getAutoCommit();
		try {
			cn.setAutoCommit(false);
			stmt = cn.createStatement();
			for (int i = 0; i < sql.length; i++) {
				if(!NVL.isEmpty(sql[i])){
					stmt.addBatch(sql[i]);
				}
			}
			stmt.executeBatch();
			
			cn.commit();
		} catch (Exception e) {
			cn.rollback();
			if (stmt != null){
				stmt.close();
				stmt=null;
			}
			e.printStackTrace();

			if(!rollbackAll){
				stmt = cn.createStatement();
				for (int i = 0; i < sql.length; i++) {
					try {
						if(!NVL.isEmpty(sql[i])){
							stmt.execute(sql[i]);
							cn.commit();
						}
					}catch(SQLIntegrityConstraintViolationException e2){
						
					} catch (Exception e2) {
						cn.rollback();
						errorCounter++;
						if(!NVL.isEmpty(logExceptionSql)){
							logExceptionSql=AIPUtil.replaceString(logExceptionSql, logTableIdName, tableIds[i]);
							logExceptionSql=AIPUtil.replaceString(logExceptionSql, logDescName, AIPUtil.getExceptionAllMessages(e2));
							try{
								stmt.executeUpdate(logExceptionSql);
								cn.commit();
							}catch(Exception e3){
								e3.printStackTrace();
							}
						}else{
							throw new Exception(sql[i], e2);
						}
					}					
				}
			}else{
				errorCounter=sql.length;
				if(!NVL.isEmpty(logExceptionSql)){
					logExceptionSql=AIPUtil.replaceString(logExceptionSql, logTableIdName, tableIds[0]);
					logExceptionSql=AIPUtil.replaceString(logExceptionSql, logDescName,"<<rollbackAll>> :\n " +AIPUtil.getExceptionAllMessages(e));
					try{
						stmt.executeUpdate(logExceptionSql);
						cn.commit();
					}catch(Exception e3){
						e3.printStackTrace();
					}
				}else{
					throw new Exception(AIPUtil.mergeSelectedIds(sql, ";"), e);
				}
			}
		} finally {
			cn.setAutoCommit(autoCommit);
			if (stmt != null)stmt.close();
			stmt=null;
		}
		return errorCounter;
	}


	public String getEtlSourceDS(){
		System.out.println("ETLAbstract.getEtlSourceDS():catalina.base="+System.getProperty("catalina.base"));
		if(System.getProperty("catalina.base")!=null){
			return NVL.getString(AIPConfiguration.getProperty("bip.etl.etlSourceDS"),"etlSourceDS");
		}else{
			return AIPConfiguration.getProperty("bip.etl.etlSourceDS","etlSourceDS");
		}
	}
	public String getEtlDestinationDS(){
		System.out.println("ETLAbstract.getEtlDestinationDS():catalina.base="+System.getProperty("catalina.base"));
		if(System.getProperty("catalina.base")!=null){
			return NVL.getString(AIPConfiguration.getProperty("bip.etl.etlDestinationDS"),"etlDestinationDS");
		}else{
			return NVL.getString(AIPConfiguration.getProperty("bip.etl.etlDestinationDS"),"etlDestinationDS");
		}
	}

	public String getOlapProcessUrl(){
		return NVL.getString(AIPConfiguration.getProperty("bip.etl.olapprocess.url"),"");
	}
	public String getOlapProcessDatabase(){
		return NVL.getString(AIPConfiguration.getProperty("bip.etl.olapprocess.database"),"Cube");
	}
	public String getOlapProcessUsername(){
		return NVL.getString(AIPConfiguration.getProperty("bip.etl.olapprocess.username"),"");
	}
	public String getOlapProcessPassword(){
		return NVL.getString(AIPConfiguration.getProperty("bip.etl.olapprocess.password"),"");
	}

	
	protected String getETLFileDatabaseRelatedSuffix(){
		try {
			return NVL.getString(AIPConfiguration.getProperty("bip.etl.xmlfile.suffix"),"");
		} catch (Exception e) {
			return "";
		}
	}
	private Properties getQueries(){
		if(queries==null){
			queries=new Properties();
			try {
				InputStream in =null;
				//this.getClass().getResource("").e
				
				try {
					in = this.getClass().getResourceAsStream(getXmlResourceName());
				} catch (Exception e) {
					String xmlResourceNameReplace = this.getClass().getSimpleName()+".xml";
					in = this.getClass().getResourceAsStream(xmlResourceNameReplace);
				}
//				File f = new File(this.getClass().getResource(resourcename).getFile());
//				if(f.exists()){
//					in = this.getClass().getResourceAsStream(resourcename);
//				}else{
//					resourcename = this.getClass().getSimpleName()+".xml";
//					in = this.getClass().getResourceAsStream(resourcename);
//				}

				queries.loadFromXML(in);
			} catch (IOException e) {
				e.printStackTrace();
				queries=null;
			}
		}
		return queries;
	}
	public String getQuery(String name,String find,String replacement){
		StringBuffer query = new StringBuffer(NVL.getString(getQueries().getProperty(name)));
		/*
		 * find and replacement
		 */
		if(!NVL.isEmpty(find)){
			if(":maxid".equalsIgnoreCase(find) || ":id".equalsIgnoreCase(find) ){
				AIPUtil.replaceAllString(query, find+"_int", ""+NVL.getInt(replacement));
			}
			AIPUtil.replaceAllString(query, find, replacement);
		}
		/*
		 * codemelli and tarikh
		 */
		if(processParam!=null){
			if(!NVL.isEmpty(processParam.getTarikhaz()) && !NVL.isEmpty(processParam.getTarikhta()) ){
				//int
				AIPUtil.replaceAllString(query, ":fltr_tarikhaz_int", processParam.getTarikhaz().replaceAll("/", ""));
				AIPUtil.replaceAllString(query, ":fltr_tarikhta_int", processParam.getTarikhta().replaceAll("/", ""));
				//miladi
				DateConvert dc= new DateConvert();
				AIPUtil.replaceAllString(query, ":fltr_tarikhaz_miladi", dc.jalali2gregorian(processParam.getTarikhaz()));
				AIPUtil.replaceAllString(query, ":fltr_tarikhta_miladi", dc.jalali2gregorian(processParam.getTarikhta())+" 23:59:59");
				//full
				AIPUtil.replaceAllString(query, ":fltr_tarikhaz", processParam.getTarikhaz());
				AIPUtil.replaceAllString(query, ":fltr_tarikhta", processParam.getTarikhta()+"9");
			}
			if(processParam.getOtherParams().size()>0){
				Set<String> keys = processParam.getOtherParams().keySet();
				for(String key:keys){
					String value = processParam.getOtherParams().get(key);
					if(!NVL.isEmpty(value)){
						AIPUtil.replaceAllString(query, ":"+key, value);
					}
				}
						
			}
		}
		
		return query.toString();
	}
	public String getQueryETL(String maxid){
		return getQuery("query-etl", ":maxid", maxid);
	}
	public String getQueryETLCount(String maxid){
		return getQuery("query-count", ":maxid", maxid);
	}
	public String getQueryMaxid(){
		return getQuery("query-maxid", null, null);
	}
	public String getSQLInsert(){
		return getQuery("sql-insert", null, null);
	}

	public String getSQLUpdate(){
		return getQuery("sql-update", null, null);
	}
	
/*	public void loadExistIds(Connection cn,String query_existids_name) throws Exception{
		existIds = new HashSet<String>();
		List<Object> l = AIPDBUtil.executeQueryIds(cn, getQuery(query_existids_name, null, null));
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			existIds.add(NVL.getString(iterator.next()));
		}
	}
	public boolean isExistId(String id){
		return existIds.contains(id);
	}
*/	public boolean isExistId(Connection cn,String query_existids_name,String id)throws Exception{
		 Object res = AIPDBUtil.executeScalar(cn, getQuery(query_existids_name, ":id", id));
		 if(res!=null){//NVL.getInt(res)>0 is for count(*) that may used 
			 return true;
		 }
		 return false;
	}
	


	
}
