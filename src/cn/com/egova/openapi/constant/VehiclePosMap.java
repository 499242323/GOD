package cn.com.egova.openapi.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VehiclePosMap {
	
	public static Map<String,Boolean> map=new ConcurrentHashMap<String, Boolean>();
    public static void add(String simCardNum,Boolean isExist){
        map.put(simCardNum,isExist);
    }
    public static Boolean get(String simCardNum){
    	
    	Boolean isExist = map.get(simCardNum);
    	
    	if(isExist ==null){
    		return false;
    	}
    	
       return isExist;
    }
    public static void remove(Boolean isExist){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==isExist){
                map.remove(entry.getKey());
            }
        }
    }


}
