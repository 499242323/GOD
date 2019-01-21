package cn.com.egova.openapi.constant;

import org.apache.log4j.Logger;

import cn.com.egova.openapi.coord.CoordConvert;
import cn.com.egova.openapi.coord.CoordConvertFactory;




public class CoordConst {
	private static CoordConvert coordConvert = null;
	
	static Logger logger = Logger.getLogger(CoordConst.class);
	
	public static CoordConvert getCoordConvertor(){
		if(coordConvert==null){
			String convertType = ConfigMap.get("coordType");
			String convertParam = ConfigMap.get("coordParam");
			
			coordConvert = CoordConvertFactory.getConvert(convertType, convertParam);
		}
		return coordConvert;
	}

}
