package aip.cron;

import org.hibernate.SQLQuery;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import aip.orm.HibernateSessionFactory;

public class AIPExecuteHibSqlJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("AIPExecuteHibSqlJob.execute():start:sql="+arg0.getJobDetail().getDescription());

		try {
			SQLQuery query = HibernateSessionFactory.getSession().createSQLQuery(arg0.getJobDetail().getDescription());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("AIPExecuteHibSqlJob.execute():end.");
	}

}
