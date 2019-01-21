package cn.com.egova.openapi.coord;

/**
 * 平面坐标实体类
 * @author hwg 2016-11-14
 */
public class XYZCoordinate {
	private double x;
    private double y;
    private double z;
    
    /**
     * 默认构造函数
     */
    public XYZCoordinate()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    /**
     * @param x 横坐标
     * @param y 纵坐标
     * @param z z坐标
     */
    public XYZCoordinate(double x, double y, double z){  
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }
    
    /**
     * 获取横坐标
     * @return
     */
    public double GetX(){
    	return this.x;
    }
    
    /**
     * 获取纵坐标
     * @return
     */
    public double GetY(){
    	return this.y;
    }
    
    /**
     * 获取Z坐标
     * @return
     */
    public double GetZ(){
    	return this.z;
    }
}
