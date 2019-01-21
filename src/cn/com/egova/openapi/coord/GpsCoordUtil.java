package cn.com.egova.openapi.coord;


public class GpsCoordUtil{
	static double pi = 3.14159265358979324;
	static double a = 6378245.0;
	static double ee = 0.00669342162296594323;
	static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	////WGS-84 to GCJ-02
	public static double[] wgs2gcj(double lat, double lon) {
		if (outOfChina(lat, lon)){
			return new double[]{lat, lon};
		}
		double[] d= delta(lat,lon);

		return new double[]{lat+d[0],lon+d[1]};
	}
	
	//WGS-84 to Baidu
	public static double[] wgs2bd(double lat, double lon) {
	       double[] wgs2gcj = wgs2gcj(lat, lon);
	       double[] gcj2bd = gcj2bd(wgs2gcj[0], wgs2gcj[1]);
	       return gcj2bd;
	}
	
	//GCJ-02 to WGS
	public static double[] gcj2wgs(double lat,double lon){
		if (outOfChina(lat, lon)){
			return new double[]{lat, lon};
		}
		double[] d= delta(lat,lon);

		return new double[]{lat-d[0],lon-d[1]};
	}

	//GCJ-02 to Baidu
	public static double[] gcj2bd(double lat, double lon) {
	       double x = lon, y = lat;
	       double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
	       double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
	       double bd_lon = z * Math.cos(theta) + 0.0065;
	       double bd_lat = z * Math.sin(theta) + 0.006;
	       return new double[] {bd_lat, bd_lon };
	}
	
	//Baidu to WGS-84
	public static double[] bd2wgs(double lat,double lon){
		 double[] bd2gcj = bd2gcj(lat, lon);
	     double[] gcj2wgs = gcj2wgs(bd2gcj[0], bd2gcj[1]);
	     return gcj2wgs;
	}

	//Baidu to GCJ-02
	public static double[] bd2gcj(double lat, double lon) {
	       double x = lon - 0.0065, y = lat - 0.006;
	       double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
	       double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
	       double gg_lon = z * Math.cos(theta);
	       double gg_lat = z * Math.sin(theta);
	       return new double[] { gg_lat, gg_lon };
	}
	
	public static double distance (double latA, double logA, double latB, double logB) {
		double earthR = 6371000;
		double x = Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * Math.cos((logA - logB) * Math.PI / 180);
		double y = Math.sin(latA * Math.PI / 180) * Math.sin(latB * Math.PI / 180);
		double s = x + y;
		if (s > 1)
			s = 1;
		if (s < -1)
			s = -1;
		double alpha = Math.acos(s);
		double distance = alpha * earthR;
		return distance;
	}
	
	public static boolean outOfChina(double wgsLat,double wgsLon){
		if (wgsLon < 72.004 || wgsLon > 137.8347)
			return true;
		if (wgsLat < 0.8293 || wgsLat > 55.8271)
			return true;
		return false;
	}
	
	private static double[] delta(double lat, double lon) {
		double a = 6378245.0d;
		double ee = 0.00669342162296594323d;
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		return new double[]{dLat,dLon};
	}

	private static double transformLat(double lat, double lon) {
	       double ret = -100.0 + 2.0 * lat + 3.0 * lon + 0.2 * lon * lon + 0.1 * lat * lon + 0.2 * Math.sqrt(Math.abs(lat));
	       ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
	       ret += (20.0 * Math.sin(lon * pi) + 40.0 * Math.sin(lon / 3.0 * pi)) * 2.0 / 3.0;
	       ret += (160.0 * Math.sin(lon / 12.0 * pi) + 320 * Math.sin(lon * pi  / 30.0)) * 2.0 / 3.0;
	       return ret;
	}

	private static double transformLon(double lat, double lon) {
	       double ret = 300.0 + lat + 2.0 * lon + 0.1 * lat * lat + 0.1 * lat * lon + 0.1 * Math.sqrt(Math.abs(lat));
	       ret += (20.0 * Math.sin(6.0 * lat * pi) + 20.0 * Math.sin(2.0 * lat * pi)) * 2.0 / 3.0;
	       ret += (20.0 * Math.sin(lat * pi) + 40.0 * Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
	       ret += (150.0 * Math.sin(lat / 12.0 * pi) + 300.0 * Math.sin(lat / 30.0 * pi)) * 2.0 / 3.0;
	       return ret;
	}
	
	/**����Ϊdouble[lat,lon]*/
	public  static double[] mercator2LatLon(double x,double y){
		double lon = x / 20037508.34 * 180;
		double lat = y / 20037508.34 * 180;
		lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
		
		double[] latLng = new double[2];
		latLng[0] = lat;
		latLng[1] = lon;
		
		return latLng;
	}
	
	public  static double[] latLon2Mercator(double lat,double lon){
		double x = lon *20037508.34/180;
		double y = Math.log(Math.tan((90+lat)*Math.PI/360))/(Math.PI/180);
		y = y *20037508.34/180;
		
		return new double[]{x,y};
	}

}
