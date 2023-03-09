package bip.cron;

import aip.util.NVL;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.text.ParseException;
import java.util.Enumeration;


public class BIPCronScheluderJobServlet extends HttpServlet {
	private static final long serialVersionUID = 4788676239313099135L;
	
	ServletConfig config;
	static SchedulerFactory sf ;
	static Scheduler sche ;
	
	public void init(ServletConfig config) throws ServletException {
		System.out.println("AIPCronScheluderJobServlet.init():start.....:");
		super.init(config);
		try {
	        sf = new StdSchedulerFactory();
	        sche = sf.getScheduler();
	        sche.start();
			addJobs(config);
			addContextJobs();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		System.out.println("AIPCronScheluderJobServlet.init():end.");
	}
	
	public void destroy() {
		super.destroy();
		try {
			sche.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	//"0 0 12 * * ?" Fire at 12pm (noon) every day
    //"0/2 * * * * ?" Fire at every 2 seconds every day
    //0 0 0/2 * * ? fire every 2 hour
	public void addJobs(ServletConfig config) {
		Enumeration initParameterNames = config.getInitParameterNames();
		while(initParameterNames.hasMoreElements()){
			String name=NVL.getString(  initParameterNames.nextElement() );
        	try {
        		addJob(name, config.getInitParameter(name));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	public void addContextJobs() {
		Enumeration initParameterNames = this.getServletContext().getInitParameterNames();
		while(initParameterNames.hasMoreElements()){
			String name=NVL.getString(  initParameterNames.nextElement() );
        	try {
        		addJob(name, this.getServletContext().getInitParameter(name));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	public void addJob(String paramName,String paramValue) throws ParseException, SchedulerException, ClassNotFoundException{
    	if(paramName.startsWith("cron.")){
        	String value = NVL.getString( paramValue );
        	value=value.replaceAll("\n", " ");
        	value=value.replaceAll("\r", " ");
        	value=value.replaceAll("\t", " ");
        	String[] params=value.split(";");
        	String name_truncated = paramName.substring(5);
        	String description ="";
        	if(params.length>3){
        		for (int i = 3; i < params.length; i++) {
            		description+=params[3];
            		if(i<params.length-1)description+=";";
				}
        	}
    		//addJob(name_truncated, params[0].trim(), Class.forName(params[1].trim()), name_truncated+"Trigger", params[2].trim(),description);
    		addJob(name_truncated, params[0].trim(), this.getClass().getClassLoader().loadClass(params[1].trim()), name_truncated+"Trigger", params[2].trim(),description);
    		
    	}
	}
/*	public void addJob_quartz1(String jobName,String groupName,Class jobClass,String trigerName,String cronExpression, String description) throws ParseException, SchedulerException{
		System.out.print("AIPCronScheluder.addJob():"+jobName+";"+groupName+";"+jobClass.toString()+";"+trigerName+";"+cronExpression+";"+description);
    	JobDetail jDetail = new JobDetail(jobName, groupName,jobClass);//"UpdateLuceneJob", "NJob", UpdateLuceneJob.class);
    	jDetail.setDescription(description);
    	CronTrigger crTrigger = new CronTrigger(trigerName, groupName,cronExpression);//"cronTrigger2", "NJob", checkSiteIsALiveJob_cronExpression);//"0 0/25 * * * ?");
    	crTrigger.setDescription(description);
        sche.scheduleJob(jDetail, crTrigger);
        System.out.println("->succeed.");
	}
*/
	public void addJob(String jobName,String groupName,Class jobClass,String trigerName,String cronExpression, String description) throws ParseException, SchedulerException{
		System.out.print("AIPCronScheluder.addJob():jobName="+jobName+";groupName="+groupName+";jobClass="+jobClass.toString()+";trigerName="+trigerName+";cronExpression="+cronExpression+";description="+description);
/*    	JobDetail jDetail = new JobDetailImpl(jobName, groupName,jobClass);//"UpdateLuceneJob", "NJob", UpdateLuceneJob.class);
    	jDetail.setDescription(description);
*/		JobDetail jDetail = JobBuilder.newJob(jobClass)
			.withIdentity(jobName, groupName)
			.withDescription(description)
			.build();

/*    	CronTrigger crTrigger = new CronTrigger(trigerName, groupName,cronExpression);//"cronTrigger2", "NJob", checkSiteIsALiveJob_cronExpression);//"0 0/25 * * * ?");
    	crTrigger.setDescription(description);
*/      Trigger crTrigger = TriggerBuilder.newTrigger()
			.withIdentity(trigerName, groupName)
			.withDescription(description)
			.withSchedule(
					CronScheduleBuilder.cronSchedule(cronExpression)) //"0/5 * * * * ?"))
			.build();
			
    	
    	sche.scheduleJob(jDetail, crTrigger);
        System.out.println("->succeed.");
	}


//	public void init(ActionServlet servlet, ModuleConfig config)throws ServletException {
//		System.out.println("InitializeCronScheluderJobPlugin.init():start.....");
//		try {
//
//	        ServletContext context = null;
//
//	        context = servlet.getServletContext();
//	        
//	        if(context.getAttribute(PLUGIN_NAME_KEY)==null){
//		        PlugInConfig[] plugInConfigs = config.findPlugInConfigs(); 
//		        for (int i = 0; i < plugInConfigs.length; i++) {
//		        	if(plugInConfigs[i].getClassName().equalsIgnoreCase("aip.struts.AIPInitializeCronScheluderJobPlugin")){
//		    	        AIPCronScheluder objPlugin = new AIPCronScheluder(plugInConfigs[i].getProperties());
//		    	        context.setAttribute(PLUGIN_NAME_KEY, objPlugin);
//		        	}
//				} 
//	        }
//
//	    }
//	    catch (Exception ex) {
//	      ex.printStackTrace();
//	    }
//		System.out.println("InitializeCronScheluderJobPlugin.init():end.");
//	}

}
