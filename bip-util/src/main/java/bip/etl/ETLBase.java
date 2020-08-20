package bip.etl;

import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aip.db.AIPDBUtil;
import aip.db.AIPTableModel;
import aip.util.AIPUtil;
import aip.util.NVL;

public abstract class ETLBase extends ETLAbstract{
	String xmlResourceName = this.getClass().getSimpleName()+getETLFileDatabaseRelatedSuffix()+".xml";
	//Collection<String> existIds;
	ProcessParam processParam;

	public abstract void fillSQLParameter(StringBuffer sql, ProcessETLListener processETL, Connection cnsrc, Connection cndest, AIPTableModel rs)throws Exception;
	public abstract void update(ProcessETLListener processETL)throws Exception;
	public abstract void process(ProcessETLListener processETL)throws Exception;

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
			}else{
				maxid = NVL.getString( AIPDBUtil.executeScalar(cndest, getQuery(query_maxid_name, null, null)) );
			}
			System.out.println("maxid="+maxid);
			
			String queryCount = getQuery(query_count_name,":maxid", ""+maxid);
			String query = getQuery(query_etl_name,":maxid",""+maxid);//getQueryETL(""+maxid);
			String srcIdField = getQuery("query-src-id",null,null);

			processETL.setQuery(query);
			int totalcount = NVL.getInt( AIPDBUtil.executeScalar(cnsrc, queryCount ) ); //getQueryETLCount(""+maxid) ) );
			processETL.setTotalcount(totalcount);
			processLog.logStartProcess(cndest,maxid,totalcount,processETL.getProcessParam());
			//////////////////
			int counter=1;
			int errorCounter=0;
			processETL.setProcessStartTime(new Date());

			////////////////// param filter
			boolean hasParamFilter=false;
			if(processParam!=null 
					&& (processParam.getOtherParams().size()>0
						|| (!NVL.isEmpty(processParam.getTarikhaz()) && !NVL.isEmpty(processParam.getTarikhta()) )
						)){
				hasParamFilter=true;
			}

			
			/*
			 * this iteration fetch first parts of all data in every loop  
			 */
			boolean etlContinue = true;
			while(etlContinue){
				query = getQuery(query_etl_name,":maxid",""+maxid);//getQueryETL(""+maxid);
				rs = AIPDBUtil.executeQuery2Table(cnsrc, query);
				
				List<ETLBatch> batches = new ArrayList<ETLBatch>();
				while(rs.next()){
					ETLBatch batch = new ETLBatch();
					
					batch.sqlInsert = new StringBuffer( getQuery(sql_insert_name, null, null) );
					batch.sqlUpdate = new StringBuffer( getQuery(sql_update_name, null, null) );

					try {
						fillSQLParameter(batch.sqlInsert,processETL,cnsrc,cndest,rs);
						fillSQLParameter(batch.sqlUpdate,processETL,cnsrc,cndest,rs);

						batches.add(batch);
					} catch (Exception e) {
						e.printStackTrace();
						
						processETL.setErrorCounter(++errorCounter);
						processLog.logException(e, cndest, processParam, rs.getString(srcIdField));
					}
					maxid=rs.getString(srcIdField);
				}

				if(batches.size()>0){
					int batchErrorCounter = executeBatch(cndest,batches,false,processLog);
					errorCounter+=batchErrorCounter;
					processETL.setErrorCounter(errorCounter);
					counter+=batches.size();
					processETL.setCounter(counter);
				}else{
					etlContinue=false;
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

	private int executeBatch(Connection cn, List<ETLBatch> batches,boolean rollbackAll, ProcessLog processLog) throws Exception {
		int errorCounter=0;
		Statement stmt = null;
		ETLBatch batch=null;
		
		boolean autoCommit = cn.getAutoCommit();
		try {
			cn.setAutoCommit(false);
			stmt = cn.createStatement();
			for (int i = 0; i < batches.size(); i++) {
				batch = batches.get(i); 
				String sql = batch.sqlInsert.toString();
				if(!NVL.isEmpty(sql)){
					stmt.addBatch(sql);
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
				for (int i = 0; i < batches.size(); i++) {
					try {
						batch = batches.get(i);
						String sql = batch.sqlInsert.toString();
						if(!NVL.isEmpty(sql)){
							stmt.execute(sql);
							cn.commit();
						}
					}catch(SQLIntegrityConstraintViolationException e2){
						try {
							batch = batches.get(i);
							String sql = batch.sqlUpdate.toString();
							if(!NVL.isEmpty(sql)){
								stmt.execute(sql);
								cn.commit();
							}
						} catch (Exception e3) {
							cn.rollback();
							errorCounter++;
							executeBatch_handleException(cn,batch,processLog,e3,null,true);
						}
					} catch (Exception e2) {
						cn.rollback();
						errorCounter++;
						executeBatch_handleException(cn,batch,processLog,e2,null,false);
					}					
				}
			}else{
				errorCounter=batches.size();
				executeBatch_handleException(cn,batch,processLog,e,"<<rollbackAll>> :\n ",false);
			}
		} finally {
			cn.setAutoCommit(autoCommit);
			if (stmt != null)stmt.close();
			stmt=null;
		}
		return errorCounter;
	}
	
	private void executeBatch_handleException(Connection cn, ETLBatch batch,ProcessLog processLog, Exception e,String extra,boolean isupdate)throws Exception{
		String logExceptionSql =processLog.getLogExceptionSql(processParam,processLog.LOG_ERROR).toString();
		if(!NVL.isEmpty(logExceptionSql)){
			logExceptionSql=AIPUtil.replaceString(logExceptionSql, ":tableid", batch.tableId);
			logExceptionSql=AIPUtil.replaceString(logExceptionSql, ":logdesc", NVL.getString(extra)+AIPUtil.getExceptionAllMessages(e));
			try{
				Statement stmt = cn.createStatement();
				stmt.executeUpdate(logExceptionSql);
				cn.commit();
			}catch(Exception e3){
				e3.printStackTrace();
			}
		}else{
			throw new Exception(isupdate?batch.sqlUpdate.toString():batch.sqlInsert.toString(), e);
		}
	}

	
}

class ETLBatch {

	StringBuffer sqlUpdate;
	StringBuffer sqlInsert;
	String tableId;
	
	
}