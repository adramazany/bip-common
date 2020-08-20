package aip.logsearch;

import aip.util.AIPException;

public interface LogSearchInterface {
	public LogSearchLST getLogSearchLST(LogSearchLSTParam param) throws AIPException;
	
	public LogSearchStatisticLST getLogSearchStatisticLST(LogSearchStatisticLSTParam param) throws AIPException ;
}
