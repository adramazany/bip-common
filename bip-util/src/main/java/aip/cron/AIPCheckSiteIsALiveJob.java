package aip.cron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AIPCheckSiteIsALiveJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("AIPCheckSiteIsALiveJob.execute():starting:url="+arg0.getJobDetail().getDescription());
		
		try {
			URL url = new URL(arg0.getJobDetail().getDescription().replaceAll("_amp_", "&"));
			InputStream in = url.openStream();
			InputStreamReader isr = new InputStreamReader(in,"UTF-8");
			BufferedReader br=new BufferedReader(isr);
			String line="";
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("AIPCheckSiteIsALiveJob.execute():end.");
	}

}
