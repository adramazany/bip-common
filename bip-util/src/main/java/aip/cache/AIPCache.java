package aip.cache;

import java.util.Date;
import java.util.Hashtable;

public class AIPCache {
	static Hashtable<String, Hashtable<String, Object>> htCacheList = new Hashtable<String, Hashtable<String,Object>>();
	
	static Date lastcleared=new Date();

	private static Hashtable<String, Object> getCache(String cacheName){
		//clear all at 1 hour
		Date now = new Date();
		if(lastcleared.getTime()+3600000 < now.getTime() ){//43200000=1h*60m*60s*1000ms
			clearAll();
			lastcleared = now;
		}
		

		
		Hashtable<String, Object> cache;
		if(htCacheList.containsKey(cacheName)){
			cache = htCacheList.get(cacheName);
		}else{
			cache = new Hashtable<String, Object>();
			htCacheList.put(cacheName, cache);
		}
		
		
		return cache;
	}
	
	public static boolean containsKey(String cacheName,String key){
		if(key==null){
			return false;
		}else{
			return getCache(cacheName).containsKey(key);
		}
	}
	public static Object get(String cacheName,String key){
		return getCache(cacheName).get(key);
	}
	public static Object put(String cacheName,String key,Object value){
		return getCache(cacheName).put(key,value);
	}

	public static Object remove(String cacheName,String key){
		return getCache(cacheName).remove(key);
	}
	
	public static synchronized void clear(String cacheName){
		if(htCacheList.containsKey(cacheName)){
			getCache(cacheName).clear();
			htCacheList.remove(cacheName);
		}
	}
	public static synchronized void clearAll(){
		htCacheList.clear();
	}

}
