package aip.cron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AIPExecuteSysCmdJob implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("AIPExecuteSysCmdJob.execute():start:cmd="+arg0.getJobDetail().getDescription());

		try {
			String command = arg0.getJobDetail().getDescription();
			String cmd[] = command.split("\n");
			Process proc = null;
			StringBuffer sb = new StringBuffer();
			
			for(int i=0;i<cmd.length;i++){
				proc = Runtime.getRuntime().exec(cmd[i]);
				
		        /*
		         * for write result in console & set result in sb
		         */
				BufferedReader in = new BufferedReader( new InputStreamReader(proc.getInputStream()));  
		        String line = null;
		        
		        while ((line = in.readLine()) != null) {
		        	sb.append(line);
		        	sb.append( '\n' );
		        	System.out.println(line);  
		        }
		        sb.append("-----------------------------------------------------");
			}
		    /*
		     * write sb in fileOutputStream :result.txt
		     */
		    FileOutputStream out = null;
		    File outputFile = null;
		    try {
		    	outputFile = new File("result.txt");
				out = new FileOutputStream(outputFile);
				out.write(sb.toString().getBytes());
				out.flush();
				out.close();
			} finally {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			/*
			 * read from result.txt
			 */
			FileInputStream fis = new FileInputStream(outputFile);
			byte b[] = new byte[fis.available()];
			fis.read(b);
			String s= new String(b,"UTF-8");

			System.out.println("result="+s);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("AIPExecuteSysCmdJob.execute():end.");
	}

}


