package cn.com.egova.openapi.coord;

import org.apache.log4j.Logger;


public class CoordConvert4Params implements CoordConvert {
	/** 日志打印的Tag,便于过滤日志. */
	private  Logger logger = Logger.getLogger(CoordConvert4Params.class);	
	
	public static int k0 = 1;
	public static final double[] MA = {6378245, 6378140, 6378137};
	public static final double[] MB = {6356863.0188, 6356755.2882, 6356752.3142};
	public static final int COORDSYSTYPE_BEIJING54 = 0;
	public static final int COORDSYSTYPE_XIAN80 = 1;
	public static final int COORDSYSTYPE_WGS84 = 2;
	private double L = 0;  //经度°′″形式
	private double B = 0;  //纬度°′″形式
	private double a = 0;  //长轴
	private double b = 0;  //短轴
	private double L0 = 0; //中央经度，弧度
	private static int coordSysID = 1;//坐标系：0:北京54采用;1:西安80; 2:WGS 84
	
    private static double DX = 0;
    private static double DY = 0;
    private static double T = 0;
    private static double K = 1; 
    
    private static boolean isXYChange =false; //是否需要X与Y对调
 
	/**
	 * 高斯转换初始化
	 * @param sysID : 坐标系表示。0:北京五四；1:西安80。目前只支持这两种
	 * @param L0：中央子午线经度值。
	 */
	public CoordConvert4Params(int sysID, String params) {
	
		 
		coordSysID = sysID;
		// this.L0 = L0 / 180 * Math.PI;
		a = MA[coordSysID];
		b = MB[coordSysID];

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
					L0 = Double.parseDouble(pams[5].trim())/ 180 * Math.PI;
				}  
			} catch (Exception e) {
				logger.error("params="+params+" error="+e.getMessage());
			} 
		}else{
			logger.debug( "params is null");
		} 
		logger.debug( "sysID="+sysID+",DX="+DX+",DY="+DY+",T="+T+",K="+K+",isXYChange="+isXYChange+",L0="+L0);
	}

	/**
	 * 把度分秒转化为度
	 * @param dfm double
	 * @return double
	 */
	public static double transDFMtoDegree(double dfm) {
		double d = Math.floor(dfm);
		double f = 1.0 * (Math.floor(dfm * 100) - d * 100) * 1.0 / 60.0;
		double m = (dfm * 10000 - Math.floor(dfm * 100) * 100.0) / 3600.0;
		double value = d + f + m;
		return value;
	}
	
	/**
	 * 把度分格式转化为度度格式
	 * @param dfm double
	 * @return double
	 */
	public static double transDFtoDegree(double df) {
		double d = Math.floor(df);
		double f = (df-d)*100.0 / 60.0;		
		double value = d + f ;
		return value;
	}
 
	 
    /**
     * 获取3度带中央子午线
     * @return
     */
    private double getMeridian(double val2) {
        if (val2 < 0) {
            return -1;
        }
        double L1 = val2 * 180 / Math.PI;
        int L2 = (int)L1;
        double left = L1 - L2;
        int num = L2 / 3;
        left += L2 % 3;
        if (left >= 1.5) num++;
        return num * 3;
    }
    

	public  double getRad(double d) {
		return d / 180 * Math.PI;
	}

	/**
	 * 获得扁率 (a-b)/a
	 * @return double
	 */
	public  double getFlattening() {
		return (a - b) / a;
	}

	/**
	 * 获得第一偏心率 对（1-(b/a)*(b/a)）求根
	 * @return double
	 */
	public  double getEccentricity() {
		return Math.sqrt(1 - Math.pow(b / a, 2));
	}

	/**
	 * 获得第二偏心率 对((a/b)*(a/b)-1)求根
	 * @param coordSysID int
	 * @return double
	 */
	public  double getSEccentricity() {
		double temp = a * 1.0 / b;
		//System.out.println("getSEccentricity  a/b = " + temp);
		//System.out.println("e' = " + Math.sqrt(temp * temp - 1));
		return Math.sqrt(temp * temp - 1);
	}

	/**
	 * 获得卯酉圈曲率半径 a/对(1-(e*e) * (sinB*sinB))求根
	 * @param coordSysID int
	 * @param B double
	 * @return double
	 */
	public  double getN() {
		double e = getEccentricity();
		return a / Math.sqrt(1 - Math.pow(e, 2) * Math.pow(Math.sin(B), 2));
	}

	/**
	 * tgB * tgB
	 * @param coordSysID int
	 * @param B double
	 * @return double
	 */
	public  double getT() {
		return Math.pow(Math.tan(B), 2);
	}

	public  double getC() {
		double se = getSEccentricity();
		//System.out.println("\tse*se = " + se * se);
		return Math.pow(se, 2) * Math.pow(Math.cos(B), 2);
	}

	public  double getA() {
		return (L - L0) * Math.cos(B);
	}

	public  double getM() {
		double e = getEccentricity();
		double temp1 = (1 - Math.pow(e, 2) / 4
						- 3 * Math.pow(e, 4) / 64
						- 5 * Math.pow(e, 6) / 256);

		//System.out.println("\tpart1的系数："  + temp1);
		double part1 = (1 - Math.pow(e, 2) / 4
						- 3 * Math.pow(e, 4) / 64
						- 5 * Math.pow(e, 6) / 256) * B;
		double part2 = (3 * Math.pow(e, 2) / 8
						+ 3 * Math.pow(e, 4) / 32
						+ 45 * Math.pow(e, 6) / 1024) * Math.sin(2 * B);
		double part3 = (15 * Math.pow(e, 4) / 256
						+ 45 * Math.pow(e, 6) / 1024) * Math.sin(4 * B);
		double part4 = (35 * Math.pow(e, 6) / 3072) * Math.sin(6 * B);
		//System.out.println("\t 计算M part1 = " + part1 + ";part2 = " + part2 + ";part3 = " + part3 + "; part4 = " + part4);
		return a * (part1 - part2 + part3 - part4);
	}

	/**
	 * 获取投影坐标
	 * @param lValue：经度 度分格式 
	 * @param bValue：纬度 度分格式
	 * @return coord[0]:纵坐标值 coord[1]:横坐标值
	 */
	public double[] convert(double bValue,double lValue) {	
		
		
 	    //double l1 = transDFMtoDegree(lValue);
 		//double b1 = transDFMtoDegree(bValue);
		//double l1 = transDFtoDegree(lValue);
		//double b1 = transDFtoDegree(bValue);
		double l1 = lValue;
		double b1 = bValue;
		double l2 = getRad(l1);
		double b2 = getRad(b1);
		
		if(this.L0==0){
		    this.L0 = getMeridian(l2) / 180 * Math.PI;	
		} 
		
		//System.out.println("l2=" + l2 + "; b2=" + b2);
		L = l2;
		B = b2;

		double M = getM();
		//System.out.println("M=" + M);
		double A = getA();
		//System.out.println("A=" + A);
		double N = getN();		
		//System.out.println("N=" + N);
		double T = getT();	
		//System.out.println("T=" + T);
		double C = getC();	
		//System.out.println("C=" + C);
		double FE = 500000;	
		//System.out.println("FE=" + FE);
		double part1 = M;
		double temp1 = Math.pow(A, 2) / 2
			+ (5 - T + 9 * C + 4 * Math.pow(C, 2)) * Math.pow(A, 4) / 24;		
		//System.out.println("temp1=" + temp1);
		double part2 = N * Math.tan(B) * (Math.pow(A, 2) / 2
										  +
										  (5 - T + 9 * C + 4 * Math.pow(C, 2)) *
										  Math.pow(A, 4) / 24);
		//System.out.println("part2=" + part2);
		double part3 = (61 - 58 * T + Math.pow(T, 2) + 270 * C - 330 * T * C) *
			Math.pow(A, 6) / 720;
		//System.out.println("part3=" + part3);
		
		double x = part1 + part2 + part3;
		part1 = FE;
		part2 = A;
		part3 = (1 - T + C) * Math.pow(A, 3) / 6;
		double part4 = (5 - 18 * T - Math.pow(T, 2) + 14 * C - 58 * T * C) *
			Math.pow(A, 5) / 120;
		double y = part1 + k0 * N * (part2 + part3 + part4);
		//double coord[] = new double[2];
		//coord[1] = x;
		//coord[0] = y;	 
		double coord[] =trans(x,y);
		
		logger.debug( "("+bValue+","+lValue+")------>"+"("+coord[0]+","+coord[1]+")");
		return coord;
	}
	
	   /**
	    * 四参数的转换
	    * @return
	    */
       private double[] trans(double x, double y) {

	        double[] result = new double[2];
	  
	        if(isXYChange){
	        	//按现场实际情况，对调xy坐标
	            result[1] = DX + K * (x * Math.cos(T) - y * Math.sin(T));
	  	        result[0] = DY + K * (x * Math.sin(T) + y * Math.cos(T));
	        }else{
	        	result[0] = DX + K * (x * Math.cos(T) - y * Math.sin(T));
	  	        result[1] = DY + K * (x * Math.sin(T) + y * Math.cos(T));
	        }  
	        return result;
       } 
       
	
    	public static void main(String[] args) { 
    		
    		//扬中
    		//String parms="-3499906.13699482;-400077.042096482;0.000052606;0.999988243374625"; 
    		//肃宁  
    		//String parms="14215.171308#-720408.712667#0.0189779340#0.999393940410";  
    		 
    		String parms="-4777.948386#-324291.244215#-0.0126576492#0.999849754106";  
    		CoordConvert4Params ct = new CoordConvert4Params(0,parms);

    		//double l = 113.480719398;
    		//double b = 34.424442268; 
    		
    		//(32.24326225,119.80711614)------>(3586927.605645355,532474.7047446894)
    		
    		//double l =  119.482205657711;
    		//double b =  32.1353080842778; 
    		double l =  119.80712237;
    		double b =  32.24296461; 
    		double[] coords = ct.convert(b,l);
    		System.out.println("x = " + coords[0] + ",y = " + coords[1]);
    		
    		l =  119.482205657711;
    		b =  32.1353080842778;
            coords = ct.convert(b,l);
    		System.out.println("x = " + coords[0] + ",y = " + coords[1]);
    		
    		
    		l =  119.48252550162;
    		b =  32.13268035532;
            coords = ct.convert(b,l);
    		System.out.println("x = " + coords[0] + ",y = " + coords[1]);
    		
            l =  119.482203942922;
            b =  32.1355137481222; 
    		coords = ct.convert(b,l);
      		System.out.println("x = " + coords[0] + ",y = " + coords[1]);
      		     
            l =  119.482523789093;
            b =  32.13288601944;
      	    coords = ct.convert(b,l);
  		    System.out.println("x = " + coords[0] + ",y = " + coords[1]); 
    	}


}
