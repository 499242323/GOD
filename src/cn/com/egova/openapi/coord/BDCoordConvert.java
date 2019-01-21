package cn.com.egova.openapi.coord;



	




import org.apache.log4j.Logger;

import cn.com.egova.openapi.base.helper.TypeConvert;
import cn.com.egova.openapi.coord.baidu.BaiduMapCoordConvertUtil;
import cn.com.egova.openapi.coord.baidu.MyLatLng;


public class BDCoordConvert implements CoordConvert {
	
	/** 日志打印的Tag,便于过滤日志. */
	private static final String TAG = "[BDCoordConvert]";
	private Logger logger = Logger.getLogger(BDCoordConvert.class);

	int type = 1; //1百度米制坐标，2百度经纬度坐标 默认为1
 
	/**
	 */
	public BDCoordConvert( String params) {
		

		if (params != null) {
			try {
				type = TypeConvert.parseInt(params, 1);

			} catch (Exception e) {
				logger.error(TAG+ "params="+params+" error="+e.getMessage());
			} 
		}else{
			logger.debug(TAG+"params is null");
		} 
	}

	@Override
	public double[] convert(double latitude,double longitude) {	
		double[] coord = new double[2];
		double[] BDLatlng = GpsCoordUtil.wgs2bd(latitude, longitude);
		if(type==2){
			return BDLatlng;
		}
        MyLatLng latLng = BaiduMapCoordConvertUtil.convertLL2MC(new MyLatLng(BDLatlng[0], BDLatlng[1]));
        coord[0] = latLng.longitude;
        coord[1] = latLng.latitude;        
 	    
		return coord;
	}

}
