package cn.com.egova.openapi.coord;

/**
 * 四参数实体类
 * @author hwg 2016-11-14
 *
 */
public class FourPara {
	private double deltaX;
	private double deltaY;
	private double ax;
	private double scale;	
    
	/**
	 * 默认构造函数
	 */
    public FourPara()
    {
    	this.deltaX = 0;
    	this.deltaY = 0;
    	this.ax = 0;
    	this.scale = 0;
        
    }
    
    /**
     * 构造函数
     * @param deltaX X坐标平移量
     * @param deltaY Y坐标平移量
     * @param ax 平面坐标轴的旋转角度
     * @param scale 尺度因子
     */
    public FourPara(double deltaX, double deltaY, double ax, double scale)
    {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.ax = ax;
        this.scale = scale;
        
    }
    
    /**
     * 获取X坐标平移量
     * @return
     */
    public double GetDeltaX(){ 
    	return this.deltaX;
    }
    
    /**
     * 获取Y坐标平移量
     * @return
     */
    public double GetDeltaY(){
    	return this.deltaY;
    }
    
    /**
     * 平面坐标轴的旋转角度
     * @return
     */
    public double GetAx(){
    	return this.ax;
    }
    
    /**
     * 尺度因子
     * @return
     */
    public double GetScale(){
    	return this.scale;
    }  
    
}
