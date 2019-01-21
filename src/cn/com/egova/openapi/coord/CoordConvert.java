package cn.com.egova.openapi.coord;

public interface CoordConvert {
	 
	/**
	 * 将经纬坐标转换为城市坐标.
	 * 
	 * @param latitude 地理纬度坐标 度度格式
	 * @param longitude 地理经度坐标 度度格式
	 * 
	 * @return 城市坐标[x, y]
	 */
	public double[] convert(double latitude, double longitude);
	
}
