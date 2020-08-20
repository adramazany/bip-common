package bip.etl;

import java.util.Date;

public interface ProcessETLListener {
	void setException(Exception ex);
	void setTotalcount(int totalcount);
	void setCounter(int counter);
	void setQuery(String query);
	void setIsProcessed(boolean isProcessed);
	void setExceptionSQL(String sql);
	
	boolean isFullProcess();
	
	void setProcessStartTime(Date startTime);
	int estimateRemainSecond(Date curTime);
	String estimateRemainTime(Date curTime);
	
	ProcessParam getProcessParam();
	
//	void setExceptionCount(int exceptionCount);
	
	void setErrorCounter(int errorCounter);
}
