package bip.etl;

import aip.olap.AIPOlapUtil;
import aip.util.AIPConfiguration;
import aip.util.NVL;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.logging.Logger;

public class MondrianCubeFlushSpringJob extends QuartzJobBean{
	static Logger logger = Logger.getLogger(MondrianCubeFlushSpringJob.class.getName()); 

	String datasourceName;
	String datasource;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)throws JobExecutionException {
		
		String _datasource = datasource;
		if(!NVL.isEmpty(datasource)){
		}else if(!NVL.isEmpty(datasourceName)){
			_datasource = AIPConfiguration.getProperty(datasourceName);
		}else{
			_datasource = AIPConfiguration.getProperty("bip.etl.cubeDS");
		}

		logger.info("MondrianCubeFlushSpringJob called for datasource:"+_datasource);
		try {
		
			AIPOlapUtil.mondrianCacheControlFlushSchemaCache(_datasource);
		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new JobExecutionException("Error occured at flushing of cube:"+_datasource,e);
		}	
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	
	
}
