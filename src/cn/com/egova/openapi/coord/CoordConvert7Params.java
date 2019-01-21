package cn.com.egova.openapi.coord;

import org.apache.log4j.Logger;
 


public class CoordConvert7Params  implements CoordConvert {
	private Logger logger = Logger.getLogger(CoordConvert7Params.class);
	/** 日志打印的Tag,便于过滤日志. */
	private static final String TAG = "[CoordConvert7Params]";
	
	private double val1;
	private double val2;
	private double val3;
	//7参数
	private static Rotate r; 
	//4参数
    private static double DX = 0;
    private static double DY = 0;
    private static double T = 0;
    private static double K = 1; 
    private static double L0 = 0; //中央经度，弧度
  //是否需要X与Y对调
    private static boolean isXYChange =false; 
    
	private double e2;
	private double a;
	private int coordType;
	  
	/**
	 * 7参数转换初始化
	 * @param sysID : 坐标系表示。0:北京五四；1:西安80。目前只支持这两种
	 * @param L0：中央子午线经度值。度度格式
	 */
	public CoordConvert7Params(int sysID, String params) {
		
		this.coordType=sysID; 
		double x0, y0, z0, rx0, ry0, rz0, m;
		
		String[] ps = params.split("@");
		//7参数
		if (ps.length>=1) {
			try {
				String[] pams = ps[0].split("#"); 
				x0  = Double.parseDouble(pams[0].trim());
				y0  = Double.parseDouble(pams[1].trim());
				z0  = Double.parseDouble(pams[2].trim());
				rx0 = Double.parseDouble(pams[3].trim());
				ry0 = Double.parseDouble(pams[4].trim());
				rz0 = Double.parseDouble(pams[5].trim());
				m   =Double.parseDouble(pams[6].trim());
				Rotate rr = new Rotate(x0, y0, z0, rx0, ry0, rz0, m); 
				this.r  = rr;
				logger.debug("sysID="+sysID+",x0="+x0+",y0="+y0+",z0="+z0+",rx0="+rx0+",ry0="+ry0+",rz0="+rz0+",m="+m);
			} catch (Exception e) {
				logger.error("params="+params+" error="+e.getMessage());
			} 
		}else{
			logger.debug( "params 7 is null");
		} 
		 
		
		//4参数
		if (ps.length>=2) {
			try {
				String[] pams = ps[1].split("#");
				
				if(pams.length==1){
					if(pams[0].trim().equalsIgnoreCase("1"))
						isXYChange = true;
				}else{
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
				} 
			} catch (Exception e) {
				logger.error("params="+params+" error="+e.getMessage());
			} 
		}else{
			logger.debug( "params 4 is null");
		} 
		logger.debug( "sysID="+sysID+",DX="+DX+",DY="+DY+",T="+T+",K="+K+",isXYChange="+isXYChange+",L0="+L0);
		 
	
	}
	
	 public double[] convert(double bValue, double lValue){

	
			this.val1 = bValue;
			this.val2 = lValue;
			this.val3 = 0;
		    //7参数转换
			this.doConvert();
			double resX = -1, resY= -1;
			resX = this.getConvertX();
			resY = this.getConvertY();
		    //resZ = this.getConvertZ();
		    //4参数转换
			double coord[] =trans(resX,resY); 
			logger.debug("("+bValue+","+lValue+")------>"+"("+coord[0]+","+coord[1]+")");
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
	
	
	/**
	 * 设置坐标类型
	 * @param coordType 1:WGS84 2:BJ54 3:C80
	 */
	private void setCoordType(int coordType){
		switch(coordType){
			case CoordConst.CoordType_WGS84: 
				this.e2 = CoordConst.WGS84_E2;
				this.a  = CoordConst.WGS84_A;
				break;
			case CoordConst.CoordType_BJ54:
				this.e2 = CoordConst.BJ54_E2;
				this.a  = CoordConst.BJ54_A;
				break;
			case CoordConst.CoordType_C80:
				this.e2 = CoordConst.C80_E2;
				this.a  = CoordConst.C80_A;
				break;
		}
	}
	
	/**
	 * 获取3度带中央子午线
	 * @return
	 */
	private double getMeridian(){
		if(val2 < 0)
		{
			return -1;
		}
		double L1 = val2 * 180/Math.PI;
		int L2 = (int)L1;
		double left = L1 - L2;
		int num = L2 / 3;
		left += L2 % 3;
		if (left>=1.5) num++;
		return num*3;
	}
	
	private void space2Space(Rotate r){
		double X = val1;
		double Y = val2;
		double Z = val3;
            val1 = (1 + r.getM()) * X + (r.getRz() * Y - r.getRy() * Z) + r.getDx();
            val2 = (1 + r.getM()) * Y + (-r.getRz() * X + r.getRx() * Z) + r.getDy();
            val3 = (1 + r.getM()) * Z + (r.getRy() * X - r.getRx() * Y) + r.getDz();
	}
	
	/**
	 * 大地坐标转空间坐标
	 * BLH->XYZ
	 */
	private void earth2Space(){	
		double B = val1;
		double L = val2;
		double H = val3;
		double W = Math.sqrt(1 - e2 * Math.sin(B) * Math.sin(B));
		double N = a / W;
		val1 = (N + H) * Math.cos(B) * Math.cos(L);
		val2 = (N + H) * Math.cos(B) * Math.sin(L);
		val3 = (N * (1-e2) + H) * Math.sin(B);
	}
	
	/**
	 * XYZ->BLH
	 *
	 */
	private void space2Earth(){
	    double W = Math.sqrt(1 - e2 * Math.sin(val1) * Math.sin(val1));
	    double N = a / W;
		double X = val1;
		double Y = val2;
		double Z = val3;
		double m ;
		m = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2));
		val2 = Math.atan(Y/X);
		if(val2<0) val2 += Math.PI;
		double e2_ = e2/(1-e2);
		double c = a * Math.sqrt(1+e2_);
		double ce2 = c * e2;
		double k = 1 + e2_;
		double front = Z/m;
		double temp = front;
		int count = 0;
		do
		{
			front = temp;
			m = Math.sqrt(Math.pow(X,2)+Math.pow(Y,2));
			temp = Z/m + ce2*front/(m*Math.sqrt(k+Math.pow(front,2)));
			count ++;
		}
		while(Math.abs(temp - front)>Math.tan(0.0001*Math.PI/(3600*180))&&count<100000);//是否在允许误差内
		val1 = Math.atan(temp);
		if(val1<0) val1 += Math.PI;
		W = Math.sqrt(1 - e2 * Math.sin(val1) * Math.sin(val1));
	    N = a / W;
		val3 = m/Math.cos(val1) - N;
	}
	
	/**
	 * BLH高斯投影
	 * @param L0
	 */
	private void earth2Gauss(double dL0){	
		double B = val1;
		double L = val2;
		double l = L - dL0;
		double B_2 = radian2Second(B);
		double CB2 = Math.cos(B)*Math.cos(B);
		double l2 = l*l;
		double N =6399698.902 - (21562.267 - (108.973 - 0.612*CB2)*CB2)*CB2;
		double a0 =32140.404-(135.3302-(0.7092-0.0040*CB2)*CB2)*CB2;
		double a4 =(0.25 + 0.00252*CB2)*CB2 - 0.04166;
		double a6 =(0.166*CB2 - 0.084)*CB2;
		double a3 =(0.3333333 + 0.001123*CB2)*CB2 - 0.1666667;
		double a5 =0.0083- (0.1667- (0.1968 + 0.004*CB2)*CB2)*CB2;
		val1 = 6367558.4969*B_2/CoordConst.P_2-(a0-(0.5 + (a4 + a6*l2)*l2)*l2*N)*Math.sin(B)*Math.cos(B);
		val2 = (1+(a3+a5*l2)*l2)*l*N*Math.cos(B);	
		val2+= 500000;
	}
	
	/**
	 * 高斯投影到地理坐标
	 * @param L0
	 */
	private void gauss2Earth(double dL0){
		val2 -= 500000;
		double x = val1;
		double y = val2;
		double b = x/6367558.4969;
		double b_2 = b*180*3600/Math.PI;//b_2以秒为单位
		double Cb2 = Math.cos(b)*Math.cos(b);
		double Bf_2 = b_2+(50221746+(293622+(2350+22*Cb2)*Cb2)*Cb2)*Math.pow(10,-10)*Math.sin(b)*Math.cos(b)*CoordConst.P_2;
	    double Bf = Bf_2*Math.PI/(180*3600);
		double CBf2 = Math.cos(Bf)*Math.cos(Bf);
		double Nf = 6399698.902-(21562.267-(108.973-0.612*CBf2)*CBf2)*CBf2;
		double Z = y /(Nf*Math.cos(Bf));
		double b2 = (0.5+0.003369*CBf2)*Math.sin(Bf)*Math.cos(Bf);
		double b3 = 0.333333-(0.166667-0.00123*CBf2)*CBf2;
		double b4 = 0.25+(0.16161+0.00562*CBf2)*CBf2;
		double b5 = 0.2-(0.1667-0.0088*CBf2)*CBf2;
		double Z2 = Z*Z;
		val1 = Bf_2-(1-(b4-0.12*Z2)*Z2)*Z2*b2*CoordConst.P_2;
		val1 = val1*Math.PI/(180*3600);
		double l= (1-(b3-b5*Z2)*Z2)*Z*CoordConst.P_2;
		l = l*Math.PI/(180*3600);
		val2 = dL0 + l;	
	}
	
	/**
	 * 弧度到秒
	 * @param r
	 * @return
	 */
	private double radian2Second(double r){
		return r*3600*180/Math.PI;
	}
	
	/**
	 * 弧度到度分秒
	 * @param r
	 * @return
	 */
	private double radian2DMS(double r){
	   double t = r*180/Math.PI;
	   int d = (int)t;
	   int m = (int)((t - d ) * 60);
	   double s = (((t-d)*60)-m)*60;
	   if(60.0-s<0.01)
	   {
		   s = 0.0;
		   m ++;
		   if(m ==60)
		   {
			   d++;
			   m = 0;
		   }
	   }
	   return ((double)d+(double)m*0.01+(double)s*0.0001);
	}
	
	/**
	 * 度分秒到弧度
	 * @param dms
	 * @return
	 */
	private double DMS2Radian(double dms){
		int d = (int)dms;
		int m = (int)((dms -d) * 100);
		double s = ((dms - d) * 100 - m) * 100;
		return ((double)d + (double)m/60 + s/3600)*Math.PI/180;
	}
	
	/**
	 * 计算坐标投影变换7参数
	 * @param m
	 * @param r
	 */
	public void CmpRotate(Matrix m, Rotate r) {
		int n = m.getNNumRows();
		Matrix B=new Matrix(3*n, 7);
		Matrix L=new Matrix(3*n, 1);
		for(int i = 0; i<n; i++)
		{
			double a[] = {1, 0, 0, m.getElement(i, 0), 0, -m.getElement(i, 2),m.getElement(i, 1)};
			double b[] = {0, 1, 0, m.getElement(i, 1), m.getElement(i, 2),0,-m.getElement(i, 0)};
			double c[] = {0, 0, 1, m.getElement(i, 2), -m.getElement(i, 1),m.getElement(i, 0), 0};
			B.setRow(3*i, a);
			B.setRow(3*i+1, b);
			B.setRow(3*i+2, c);
			L.setElement(3*i, 0, m.getElement(i, 3));
			L.setElement(3*i+1, 0, m.getElement(i, 4));
			L.setElement(3*i+2, 0, m.getElement(i, 5));
		}  
		Matrix R=new Matrix(7, 1);
		R = Matrix.Mup(Matrix.Mup(Matrix.Mup(B.T(), B).Getinv(), B.T()), L);
		r.setDx(-R.getElement(0, 0));
		r.setDy(-R.getElement(1, 0));
		r.setDz(-R.getElement(2, 0));
		r.setRx(1-R.getElement(3, 0));
		r.setRy(-R.getElement(4, 0)/R.getElement(3, 0));
		r.setRz(-R.getElement(5, 0)/R.getElement(3, 0));
		r.setM(-R.getElement(6, 0)/R.getElement(3, 0));
	
	}
	
	/**
	 * 由WGS84到BJ54坐标转换
	 * @return
	 */
	public boolean doConvert(){
		try{
			this.setCoordType(CoordConst.CoordType_WGS84);
			//LBS已经是度度格式，不需要转化
			//this.val1 = DMS2Radian(this.val1);
			//this.val2 = DMS2Radian(this.val2);
			//this.val3 = DMS2Radian(this.val3);
			//this.val1 = LBSTool.transDFM2DD(this.val1)*Math.PI/180;
			//this.val2 = LBSTool.transDFM2DD(this.val2)*Math.PI/180;
            this.val1 = this.val1*Math.PI/180;
			this.val2 = this.val2*Math.PI/180;
			this.earth2Space();
			this.space2Space(this.r);
			this.setCoordType(this.coordType);
			this.space2Earth();
			
			double n=0;
			if(L0 > 0){
				n= L0;
			}else{
				double m = getMeridian();
				if(m < 0){ 
					return false;
				}
			    n = DMS2Radian(m);
			} 
			
			this.earth2Gauss(n);
		}catch(Exception ex ){ 
			return false;
		}
		return true;
	}
	
	public double getConvertX(){
		return this.val1;
	}
	
	public double getConvertY(){
		return this.val2;
	}
	
	public double getConvertZ(){
		return this.val3;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	  
		CoordConvert7Params ks = new CoordConvert7Params(CoordConst.CoordType_C80,"-6.467988#14.872429#11.750627#0.0000046479#-0.0000091573#0.0000151194#-0.000002672626@1"); 
		double[] result1 = ks.convert(36.085738395,114.183633138);
		System.out.println("result[0] = " + result1[0] + "; result[1] = " + result1[1]);
		
		double[] result2 = ks.convert(36.065031308,114.143925056);
		System.out.println("result[0] = " + result2[0] + "; result[1] = " + result2[1]);
		
		double[] result6 = ks.convert(36.025475799,114.204610522);
		System.out.println("result[0] = " + result6[0] + "; result[1] = " + result6[1]);
		
		
	}
	
}
