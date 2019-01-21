package cn.com.egova.openapi.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigMap {
	
	public static Map<String,String> map=new ConcurrentHashMap<String, String>();
    public static void add(String key,String value){
        map.put(key,value);
    }
    public static String get(String key){
    	
    	return map.get(key);
    	
    	
    }
    public static void remove(String value){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==value){
                map.remove(entry.getKey());
            }
        }
    }

}
