package aip.cron;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.hibernate.SQLQuery;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import aip.orm.HibernateSessionFactory;
import aip.util.AIPUtil;
import aip.util.NVL;

public class AIPExecuteJdbcSqlJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("AIPExecuteJdbcSqlJob.execute():start:sql="+arg0.getJobDetail().getDescription());

		Connection cn = null;
		Statement stmt = null;
		try {
			Class.forName(HibernateSessionFactory.getProperty(""));
			cn = DriverManager.getConnection(HibernateSessionFactory.getProperty("")
					, HibernateSessionFactory.getProperty(""), HibernateSessionFactory.getProperty(""));

			stmt = cn.createStatement();
			String[] sqls = AIPUtil.splitSelectedIds(arg0.getJobDetail().getDescription(), ";");
			for (int i = 0; i < sqls.length; i++) {
				if(!NVL.isEmpty(sqls[i])){
					try {
						stmt.execute(sqls[i]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(stmt!=null)stmt.close();
				if(cn!=null)cn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		System.out.println("AIPExecuteJdbcSqlJob.execute():end.");
	}

}
