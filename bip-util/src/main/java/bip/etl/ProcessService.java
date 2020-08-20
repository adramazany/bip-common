package bip.etl;

import java.util.Hashtable;

import aip.olap.AIPOlapUtil;
import aip.util.AIPConfiguration;
import aip.util.AIPException;
import aip.util.AIPWebUserParam;
import aip.util.NVL;

public class ProcessService implements ProcessInterface {
	public static final int PrerequisitMaxCount = 20;

	static Hashtable<String, ProcessETLInfo> htProcessETLInfos = new Hashtable<String, ProcessETLInfo>();

	public void startProcess(ProcessParam param) throws AIPException {
		if (!htProcessETLInfos.containsKey(param.getTablename())) {
			htProcessETLInfos.put(param.getTablename(), new ProcessETLInfo());
		}
		final ProcessETLInfo processETLInfo = htProcessETLInfos.get(param.getTablename());
		 
		//if (processETLInfo.getEtlLock().tryLock())
		if (processETLInfo.getStatus()!=ProcessETLInfo.STAT_RUNNING)
		{
			processETLInfo.reset();
			processETLInfo.setProcessParam(param);
			processETLInfo.setFullProcess(param.getIsfullprocess());
			
			int prerequisitCounter =0;
			while(isPrerequisitRunning(param.getTablename())){
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					processETLInfo.setStatus(ProcessETLInfo.STAT_ERROR);
					processETLInfo.setException(e);
					e.printStackTrace();
					throw new AIPException(e);
				}
				prerequisitCounter++;
				if(prerequisitCounter>PrerequisitMaxCount){
					AIPException ex = new AIPException("زمان انتظار برای اتمام پیش نیازهای جدول "+param.getTablename()+" بیش از اندازه شده است!");
					processETLInfo.setStatus(ProcessETLInfo.STAT_ERROR);
					processETLInfo.setException(ex);
					throw ex;
				}
			}

			final ETLAbstract etlObject = getETLObject(param.getTablename());
			
			new Thread(){
				public void run() {
					try {
						processETLInfo.setStatus(ProcessETLInfo.STAT_RUNNING);
						etlObject.update(processETLInfo);
						
						if(AIPConfiguration.getProperty("bip.etl.mdx.mdxfile.suffix").contains("mondrian")){
							processETLInfo.setIsProcessed(true);
							
							String datasource = AIPConfiguration.getProperty("bip.etl.cubeDS");
							AIPOlapUtil.mondrianCacheControlFlushSchemaCache(datasource);
						}else{
							etlObject.process(processETLInfo);
						}
					} catch (Exception e) {
						processETLInfo.setStatus(ProcessETLInfo.STAT_ERROR);
						processETLInfo.setException(e);
					}finally{
						processETLInfo.setStatus(ProcessETLInfo.STAT_FINISHED);
					}
				};
				
			}.start();
		} else {
			throw new AIPException("تجمیع و پردازش "+param.getTablename()+" در حال اجرا می باشد");
		}
	}
	
	@Deprecated
	public void startProcessCube() throws AIPException {
		startProcessCube(AIPConfiguration.getProperty("aip.sabtbi.cubeDS"));
	}
	public void startProcessCube(String datasource) throws AIPException {
		if(NVL.isEmpty(datasource)){
			datasource = AIPConfiguration.getProperty("bip.etl.cubeDS");
		}
		if(NVL.isEmpty(datasource)){
			datasource = AIPConfiguration.getProperty("bip.sabtbi.cubeDS");
		}
		AIPOlapUtil.mondrianCacheControlFlushSchemaCache(datasource);
	}
	
	public boolean isPrerequisitRunning(String processingTablename){
		String prerequisitsStr = AIPConfiguration.getProperty("bip.etl.table_prerequisits."+processingTablename, "");
		if(!NVL.isEmpty(prerequisitsStr)){
			String[] prerequisits =prerequisitsStr.split(",");
			if(prerequisits!=null){
				ProcessParam param = new ProcessParam();
				for (int i = 0; i < prerequisits.length; i++) {
					param.setTablename(prerequisits[i]);
					ProcessETLInfo info = getProcessETLInfo(param);
					if(info!=null && info.getStatus()==ProcessETLInfo.STAT_RUNNING){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public ProcessETLInfo getProcessETLInfo(ProcessParam param){
		ProcessETLInfo info = null;
		if (htProcessETLInfos.containsKey(param.getTablename())) {
			info = htProcessETLInfos.get(param.getTablename());
//			System.out.println(":::::::::::::::::::::ProcessDAO.getProcessETLInfo():status"+info.getStatus());
		}
		return info;
	}
	

	private ETLAbstract getETLObject(String tablename) {
		ETLAbstract etlObject =null;
		String classname = AIPConfiguration.getProperty("bip.etl.table2etl."+tablename);
		try {
			etlObject = (ETLAbstract)Class.forName(classname).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return etlObject;
	}

	public static void main(String[] args) {
		System.out.println("ProcessDAO.main():start........");
		
		ProcessService processService = new ProcessService();

		ProcessParam param=new ProcessParam();
		param.setReqCode("startProcess");
		param.setTablename("table2");
		param.setIsfullprocess(false);
		//param.setTarikhaz(NVL.getString(request.getParameter("tarikhaz")));
		//param.setTarikhta(NVL.getString(request.getParameter("tarikhta")));
		param.setIgnoreErrorAndLog(true);
		param.setWebUserParam(new AIPWebUserParam("test"));
		//param.setTableId(NVL.getString(request.getParameter("tableid")));
		//param.getOtherParams().put(otherParam, request.getParameter(otherParam));
		try {
			processService.startProcess(param);
		} catch (AIPException e) {
			e.printStackTrace();
		}
		
		System.out.println("ProcessDAO.main():finished.");
	}

}
