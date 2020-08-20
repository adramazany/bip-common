package bip.etl;

import java.util.Date;

public class DefaultProcessETLListener implements ProcessETLListener {
	boolean fullProcess;
	
	public DefaultProcessETLListener(){}
	public DefaultProcessETLListener(boolean fullProcess){
		this.fullProcess=fullProcess;
	}
	
	public void setTotalcount(int totalcount) {
		System.out.println("DefaultProcessETL.setTotalcount():"+totalcount);
	}
	public void setQuery(String sql) {
		System.out.println("DefaultProcessETL.setSql():"+sql);
	}
	public void setException(Exception ex) {
		System.out.println("DefaultProcessETL.setException():"+ex.getMessage());
	}
	public void setCounter(int current) {
		System.out.println("DefaultProcessETL.setCurrent():"+current);
	}
	public void setIsProcessed(boolean isProcessed){
		System.out.println("DefaultProcessETL.setIsProcessed():"+isProcessed);
	}
	public void setExceptionSQL(String sql) {
		System.out.println("DefaultProcessETL.setExceptionSQL():"+sql);
	}

	public boolean isFullProcess() {
		return fullProcess;
	}
	public void setFullProcess(boolean fullProcess) {
		this.fullProcess = fullProcess;
	}

	public void setProcessStartTime(Date startTime) {
		System.out.println("DefaultProcessETLListener.setProcessStartTime():"+startTime);
	}
	public int estimateRemainSecond(Date curTime) {
		System.out.println("DefaultProcessETLListener.estimateRemainSecond():"+curTime);
		return 0;
	}
	public String estimateRemainTime(Date curTime) {
		System.out.println("DefaultProcessETLListener.estimateRemainTime():"+curTime);
		return "";
	}
	public ProcessParam getProcessParam() {
		return new ProcessParam();
	}

	public void setErrorCounter(int errorCounter) {
		System.out.println("DefaultProcessETL.setErrorCounter():"+errorCounter);
	}

	
	
}
