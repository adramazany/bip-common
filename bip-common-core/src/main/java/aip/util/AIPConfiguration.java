package aip.util;

import bip.common.util.BIPUtil;
import bip.common.util.NVL;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.logging.Logger;

public class AIPConfiguration {
	private static Logger logger = Logger.getLogger(AIPConfiguration.class.getName());
	
	static Properties config=null;
	static String config_file1="/aip.cfg.properties";
	static String config_file2="/bip.cfg.properties";
	//static long config_file_last_modified=0;
	static long config_file_last_read_time=0;
	static long config_file_cache_time=60*1000;
	
	private static Properties getProperties(){
		//TODO : new File(config_file).lastModified() not work on weblogic
		//if(config==null || new File(config_file).lastModified()>config_file_last_modified){
		if(config==null || config_file_last_read_time+config_file_cache_time<System.currentTimeMillis()){
			config = new Properties();
			try {
				Reader reader1 = new InputStreamReader(AIPConfiguration.class.getResourceAsStream(config_file1), "UTF-8");
				if(reader1!=null)config.load( reader1 );
			} catch (Exception e) {
				logger.severe(config_file1 +" not Found! "+ BIPUtil.getExceptionAllMessages(e));
			}
			try {
				Reader reader2 = new InputStreamReader(AIPConfiguration.class.getResourceAsStream(config_file2), "UTF-8");
				if(reader2!=null)config.load( reader2 );
			} catch (Exception e) {
				logger.severe(config_file2 +" not Found! "+BIPUtil.getExceptionAllMessages(e));
			}
			config_file_last_read_time = System.currentTimeMillis();
			config_file_cache_time = NVL.getLng(config.getProperty("aip.cfg.config_file_cache_time",""+config_file_cache_time));
		}
		return config;
	}
	
	public static String getProperty(String key,String defaultValue){
		return getProperties().getProperty(key, defaultValue);
	}
	public static String getProperty(String key){
		return getProperties().getProperty(key);
	}
	

}
