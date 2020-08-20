package bip.etl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import aip.util.AIPWebUserParam;


public class ProcessETLJob implements Job {
	

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("ProcessETLJob.execute():start at: "+(new Date())+" ,tablename="+arg0.getJobDetail().getDescription());
		try {
			ProcessParam param=new ProcessParam();
			param.setTablename(arg0.getJobDetail().getDescription());
			param.setWebUserParam(new AIPWebUserParam("job-user"));
			(new ProcessService()).startProcess(param);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getRootLogger().error(e);
		}
		
		System.out.println("ProcessETLJob.execute():end.");
	}

}
