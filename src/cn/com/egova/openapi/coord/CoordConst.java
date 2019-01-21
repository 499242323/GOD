package cn.com.egova.openapi.coord;

/**
 * 坐标转换常量
 * @author Administrator
 *
 */
public class CoordConst {
	public final static double P_2 = (3600*180/Math.PI);
	public final static int    CoordType_WGS84 = 1;
	public final static double WGS84_A = 6378137.0000000000;//WGS-84椭球体长半轴a
	public final static double WGS84_E2 = 0.0066943799013;  //WGS-84椭球偏心率e的平方
	
	public final static int    CoordType_BJ54 = 2;
	public final static double BJ54_A = 6378245.0000000000;//BJ54(克拉索夫斯基)参数
	public final static double BJ54_E2 = 0.006693421622966;
	
	public final static int    CoordType_C80 = 3;
	public final static double C80_A = 6378140.0000000000;//全国1980西安坐标系(1975国际椭球体)参数
	public final static double C80_E2 = 0.006694384999588; 
 
}
