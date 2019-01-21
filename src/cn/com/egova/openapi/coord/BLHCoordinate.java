package cn.com.egova.openapi.coord;

/**
 * 经纬度坐标实体类
 * @author hwg 2016-11-14
 */
public class BLHCoordinate {
	private double longitude;
	private double latitude;   
    private double height;
    
    /**
     * 默认构造函数
     */
    public BLHCoordinate(){
    	this.longitude = 0;
    	this.latitude = 0;
    	this.height = 0;
    }
    
    /**
     *@param longitude 经度
     *@param latitude 纬度
     *@param height 高程
     */
    public BLHCoordinate(double longitude, double latitude,double height){
    	this.longitude = longitude;
    	this.latitude = latitude;
    	this.height = height;
    }
    
    /**
     * 获取经度
     * @return
     */
    public double GetLongitude(){
    	return this.longitude;
    }
    
    /**
     * 获取纬度
     * @return
     */
    public double GetLatitude(){
    	return this.latitude;
    }
    
    /**
     * 获取高程
     * @return
     */
    public double GetHeight(){
    	return this.height;
    }
}
