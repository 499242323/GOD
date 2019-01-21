package cn.com.egova.openapi.coord;

import org.apache.log4j.Logger;


/**
 * 移动测量提供的四参数，使用此转换
 * @author zhaochong
 *
 */
public class YDCLCoordConvert implements CoordConvert {

	/** 日志打印的Tag,便于过滤日志. */
	private  Logger logger = Logger.getLogger(YDCLCoordConvert.class);
    private static double DX = 0;
    private static double DY = 0;
    private static double T = 0;
    private static double K = 1; 

    private static boolean isXYChange =false; //是否需要X与Y对调
	private double L0 = 0; //中央经度，弧度
	
	double[] datum = new double[] {6378137, 6356752.3142, 0.00669437999013};


	public YDCLCoordConvert(int sysID, String params) {
		if (params != null) {
			try {
				String[] pams = params.split("#");
				DX = Double.parseDouble(pams[0].trim());
				DY = Double.parseDouble(pams[1].trim());
				T = Double.parseDouble(pams[2].trim());/// 180 * Math.PI;
				K = Double.parseDouble(pams[3].trim());
				
				if (pams.length >= 5) {
					if(pams[4].trim().equalsIgnoreCase("1"))
					isXYChange = true;
				}
				if(pams.length >= 6){
					L0 = Double.parseDouble(pams[5].trim());// / 180 * Math.PI;
				}  
			} catch (Exception e) {
				logger.error( "params="+params+" error="+e.getMessage());
			} 
		}else{
			logger.debug("params is null");
		} 
		logger.debug( "sysID="+sysID+",DX="+DX+",DY="+DY+",T="+T+",K="+K+",isXYChange="+isXYChange+",L0="+L0);
	}
	
	/**
	 * 将经纬坐标转换为城市坐标.
	 * 
	 * @param b 地理纬度坐标, 度度
	 * @param l 地理经度坐标, 度度
	 * 
	 * @return 城市坐标[x, y]
	 */
	public double[] convert(double b, double l) {
		double[] result = Para4Convert(GaussProjCal(b,l, 0));
		if (isXYChange) {
			logger.debug( "("+b+","+l+")------>"+"("+result[1]+","+result[0]+")");
			return new double[] { result[1], result[0] };
		}
		logger.debug( "("+b+","+l+")------>"+"("+result[0]+","+result[1]+")");
		return new double[] { result[0], result[1] };
	}

	/**
	 * 高斯投影由经纬度(Unit:DD)计算大地平面坐标(含带号，Unit:Metres) 
	 * @param b
	 * @param l
	 * @param h
	 * @return
	 */
    public double[] GaussProjCal(double b, double l, double h)
    {
        int ProjNo, ZoneWide; ////带宽 
        double longitude0, X0, xval, yval;
        double a, e2, ee, NN, T, C, A, M;
        ZoneWide = 3; //3度带宽 
        a = datum[0];
        ProjNo = (int)((l - 1.5) / ZoneWide + 1);
        longitude0 = L0;
        if (Math.abs(L0 - 0) < 0.000001)
        {
            longitude0 = ProjNo * ZoneWide; //中央经线
        }
        longitude0 = longitude0 * Math.PI / 180;
        l = l * Math.PI / 180; //经度转换为弧度
        b = b * Math.PI / 180; //纬度转换为弧度
        e2 = datum[2];
        ee = e2 * (1.0 - e2);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(b) * Math.sin(b));
        T = Math.tan(b) * Math.tan(b);
        C = ee * Math.cos(b) * Math.cos(b);
        A = (l - longitude0) * Math.cos(b);

        M = a * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * b 
        		- (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * b) 
        		+ (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * b) - (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * b));
        xval = NN * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72 * C - 58 * ee) * A * A * A * A * A / 120);
        yval = M + NN * Math.tan(b) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61 - 58 * T + T * T + 600 * C - 330 * ee) * A * A * A * A * A * A / 720);
        //X0 = 1000000L * ProjNo + 500000L;
        X0 =  500000L;
        xval = xval + X0;
        return new double[]{xval, yval, h};
    }

    /**
     * 利用4参数求新坐标系的坐标
     * @param aPtSource
     * @return
     */
    public static double[] Para4Convert(double[] aPtSource)
    {
        double k = K;
        double a1 = k * Math.cos(T);
        double a2 = k * Math.sin(T);

        double[] ToCoordinate = new double[3];
        ToCoordinate[0] = DX + a1 * aPtSource[0] + a2 * aPtSource[1];
        ToCoordinate[1] = DY + a1 * aPtSource[1] - a2 * aPtSource[0];
        ToCoordinate[2] = aPtSource[2];

        return ToCoordinate;
    }

	public static void main(String[] args) { 
		String parms="38001472.52375500756#3685.859375#-0.000296672764533166#0.998749958531447#0#114";
		YDCLCoordConvert ct = new YDCLCoordConvert(0,parms);

		double l =  115.51517021;
		double b =  40.4053348555;
		double[] coords = ct.convert(b,l);
		System.out.println("x = " + coords[0] + ",y = " + coords[1]);
	}
}
