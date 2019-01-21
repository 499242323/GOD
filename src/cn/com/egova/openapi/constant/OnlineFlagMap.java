package cn.com.egova.openapi.constant;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class OnlineFlagMap {
	
	public static Map<String,Date> map=new ConcurrentHashMap<String, Date>();
    public static void add(String SimCardNum,Date reportTime){
        map.put(SimCardNum,reportTime);
    }
    public static Date get(String SimCardNum){
       return map.get(SimCardNum);
    }
    public static void remove(Date reportTime){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==reportTime){
                map.remove(entry.getKey());
            }
        }
    }

}
