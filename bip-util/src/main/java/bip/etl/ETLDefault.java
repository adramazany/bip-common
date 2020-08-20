package bip.etl;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Set;

import aip.db.AIPTableModel;
import aip.util.AIPConfiguration;
import aip.util.AIPUtil;
import aip.util.NVL;

public class ETLDefault extends ETLBase {
	Hashtable<String, String> htMappings=null;

	@Override
	public void update(ProcessETLListener processETL) throws Exception {
		setXmlResourceName(processETL.getProcessParam().getTablename()+getETLFileDatabaseRelatedSuffix()+".xml");
		
		update(processETL, new ProcessLogDefault(AIPConfiguration.getProperty("bip.etl.etl_log.tablename", "etl_log")));
	}

	@Override
	public void process(ProcessETLListener processETL) throws Exception {
		processETL.setIsProcessed(true);
	}

	@Override
	public void fillSQLParameter(StringBuffer sql, ProcessETLListener processETL, Connection cnsrc,
			Connection cndest, AIPTableModel rs) throws Exception {
		
		Hashtable<String, String> mappings = getMappings(processETL.getProcessParam().getTablename());
		Set<String> keys = mappings.keySet();
		for(String key:keys){
			AIPUtil.replaceAllString(sql,key, rs.getString(mappings.get(key)) );
		}
	}

	public Hashtable<String, String> getMappings(String tablename) throws ETLException{
		if(htMappings==null){
			String mappingsStr = getQuery("mappings", "", "");
			if(NVL.isEmpty(mappingsStr)){
				throw new ETLException("قسمت mappings برای این جدول "+tablename+" در فایل xml مربوطه وارد نشده است!");
			}
			htMappings = new Hashtable<String, String>();
			String[] lines = mappingsStr.split("\n");
			for(String line:lines){
				String[] keyValue = line.split("=");
				if(keyValue.length==2){
					String key = keyValue[0]; 
					String value = keyValue[1]; 
					if(!NVL.isEmpty(key) && !NVL.isEmpty(value)){
						htMappings.put(key.trim(), value.trim());
					}
				}
			}
			
		}
		return htMappings;
		
		
	}
	
	

}
