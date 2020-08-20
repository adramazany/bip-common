package bip.etl;

import java.sql.Connection;

import bip.etl.ProcessParam;

public interface ProcessLog {
	public static final String LOG_ERROR="خطا";
	public static final String LOG_WARN="هشدار";
	public static final String LOG_INFO="اطلاع";
	
	public StringBuffer getLogExceptionSql(ProcessParam processParam, String logError);

	public void logStartProcess(Connection cndest, String maxid, int totalcount,
			ProcessParam param) ;

	public void logException(Exception ex, Connection cndest, ProcessParam param, String tableId);

	public void logExceptionResolved(Connection cndest, ProcessParam processParam, String tableId);

}
