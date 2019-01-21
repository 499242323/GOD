package cn.com.egova.openapi.coord.baidu;


/**
 * 由于百度LatLng对象在初始化时，经纬度大于latitude: 2147.483647, longitude: 2147.483647时，会自动设置该值，
 * 影响百度米制坐标转经纬度坐标，因此定义这个类型来中转
 * @author zhw
 * @2015-6-19
 */
public class MyLatLng {
    public double latitude;
    public double longitude;
    public MyLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
