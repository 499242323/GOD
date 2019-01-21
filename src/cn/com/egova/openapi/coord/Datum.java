package cn.com.egova.openapi.coord;

/**
 * 基准面实体类
 * @author hwg 2016-11-14
 *
 */
public class Datum {
	private double rMajor;
    private double rMinor;
    private double e2;
    
    /**
     * 默认构建函数
     */
    public Datum()
    {
    	this.rMajor = 0;
    	this.rMinor = 0;
    	this.e2 = 0;
    }
    
    /**
     * 构造函数
     * @param r_major 长半轴
     * @param r_minor 短半轴
     * @param e2 偏心率
     */
    public Datum(double rMajor, double rMinor, double e2)
    {
    	this.rMajor = rMajor;
    	this.rMinor = rMinor;
    	this.e2 = e2;
    }
    
    /**
     * 获取长半轴
     * @return
     */
    public double GetRMajor(){
    	return this.rMajor;
    }
    
    /**
     * 获取短半轴
     * @return
     */
    public double GetRMinor(){
    	return this.rMinor;
    }
    
    /**
     * 获取偏心率
     * @return
     */
    public double GetE2(){
    	return this.e2;
    }
}
