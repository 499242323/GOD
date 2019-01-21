package cn.com.egova.openapi.coord;
import org.apache.log4j.Logger;



public class CoordXY2WGS84  implements CoordConvert{
	private double centerLon = 0; //中央经度
	private boolean isXYChange =false; //是否需要X与Y对调
	private FourPara fourPara = null; //四参数实体对象
	private Datum wgs84Datum = null;  //WGS84椭球体实体对象
	/** 日志打印的Tag,便于过滤日志. */
	private  Logger logger = Logger.getLogger(CoordXY2WGS84.class);
	
	/**
	 * 构造函数
	 * @param params
	 */
	public CoordXY2WGS84(String params){
		if (params != null) {
			try{
				String[] pams = params.split("#"); //从库中读取的四参数组合字符串
				double deltaX = Double.parseDouble(pams[0].trim());
				double deltaY = Double.parseDouble(pams[1].trim());
				double ax = Double.parseDouble(pams[2].trim());
				double scale = Double.parseDouble(pams[3].trim());
				fourPara = new FourPara(deltaX, deltaY, ax, scale);
				wgs84Datum = new Datum(6378137, 6356752.3142, 0.00669437999013);
				
				if (pams.length >= 5) {
					if(pams[4].trim().equalsIgnoreCase("1"))
					isXYChange = true;
				}
				if(pams.length >= 6){
					centerLon = Double.parseDouble(pams[5].trim());
				} 
			}catch (Exception e) {
				logger.error("params="+params+" error="+e.getMessage());
			} 
		}else{
			logger.debug( "params is null");
		}
	}
	
	/**
	 * @param x 平面横坐标X
	 * @param y 平面纵坐标Y
	 */
	public double[] convert(double x, double y){	
		try{
			XYZCoordinate wgs84xyz = Para4Convert(new XYZCoordinate(x, y, 0), fourPara);     		
            BLHCoordinate wgs84Point = GaussProjInvCal(wgs84xyz, wgs84Datum, centerLon);
            double lon = wgs84Point.GetLongitude();
            double lat = wgs84Point.GetLatitude();
            if(isXYChange){
            	return new double[]{lat, lon};
            }
            else{
            	return new double[]{lon, lat};
            }
            
		}
		catch(Exception e){
			return new double[]{-1, -1};
		}
	}
	
	/**
	 * 高斯投影由大地平面坐标(Unit:Metres)反算经纬度(Unit:DD)
	 * @param XYZ
	 * @param datum
	 * @param lon
	 * @return
	 */
    public static BLHCoordinate GaussProjInvCal(XYZCoordinate XYZ, Datum datum, double lon)
    {
        int ProjNo, ZoneWide; ////带宽 
        double l, b, longitude0, X0, xval, yval;
        double e1, e2, f, a, ee, NN, T, C, M, D, R, u, fai;
        a = datum.GetRMajor(); //54年北京坐标系参数 
        ZoneWide = 3; //3度带宽 
        ProjNo = (int)(XYZ.GetX() / 1000000L); //查找带号
        longitude0 = lon;
        if (Math.abs(lon - 0) < 0.000001)
        {
            longitude0 = ProjNo * ZoneWide; //中央经线
        }
        longitude0 = longitude0 * Math.PI / 180; //中央经线
        X0 = ProjNo * 1000000L + 500000L;
        xval = XYZ.GetX() - X0; //带内大地坐标
        yval = XYZ.GetY();
        e2 = datum.GetE2();
        e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
        ee = e2 / (1 - e2);
        M = yval;
        u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
        fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * u) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
        C = ee * Math.cos(fai) * Math.cos(fai);
        T = Math.tan(fai) * Math.tan(fai);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));

        R = a * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)));
        D = xval / NN;
        //计算经度(Longitude) 纬度(Latitude)
        l = longitude0 + (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T - 3 * C * C + 8 * ee + 24 * T * T) * D
        * D * D * D * D / 120) / Math.cos(fai);
        b = fai - (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D * D * D * D / 24
        + (61 + 90 * T + 298 * C + 45 * T * T - 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);
        //转换为度 DD
        l = l * 180 / Math.PI;
        b = b * 180 / Math.PI;
        return new BLHCoordinate(l, b, XYZ.GetZ());
    }
	

    /**
     * 利用4参数求新坐标系的坐标
     * @param sourcePoint 待转换点的坐标实体对象
     * @param fourPara 坐标转换四参数实体对象
     * @return
     */
    public static XYZCoordinate Para4Convert(XYZCoordinate sourcePoint, FourPara fourPara)
    {
        double k = fourPara.GetScale();
        double a1 = k * Math.cos(fourPara.GetAx());
        double a2 = k * Math.sin(fourPara.GetAx());
       
        double toX = fourPara.GetDeltaX() + a1 * sourcePoint.GetX() + a2 * sourcePoint.GetY();
        double toY = fourPara.GetDeltaY() + a1 * sourcePoint.GetY() - a2 * sourcePoint.GetX();
        double toZ = sourcePoint.GetZ();
        
        XYZCoordinate ToCoordinate = new XYZCoordinate(toX,toY,toZ);

        return ToCoordinate;
    }
}
