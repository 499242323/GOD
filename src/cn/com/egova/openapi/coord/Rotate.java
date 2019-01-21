package cn.com.egova.openapi.coord;

/**
 * 坐标投影变换7参数
 * @author zls
 */
public class Rotate {
	private double dx;    //X平移
	private double dy;    //Y平移
	private double dz;    //Z平移
	private double rx;    //X旋转
	private double ry;    //Y旋转
	private double rz;    //Z旋转
	private double m;     //以及尺度比参数
		
	public Rotate(){
		  
	}

	public Rotate(double a,double b,double c,double d,double e,double f,double g){
		  this.dx=a;
		  this.dy=b;
		  this.dz=c;
		  this.rx=d;
		  this.ry=e;
		  this.rz=f;
		  this.m=g;
	}
	public double getDx() {
		return dx;
	}
	public void setDx(double dx) {
		this.dx = dx;
	}
	public double getDy() {
		return dy;
	}
	public void setDy(double dy) {
		this.dy = dy;
	}
	public double getDz() {
		return dz;
	}
	public void setDz(double dz) {
		this.dz = dz;
	}
	public double getM() {
		return m;
	}
	public void setM(double m) {
		this.m = m;
	}
	public double getRx() {
		return rx;
	}
	public void setRx(double rx) {
		this.rx = rx;
	}
	public double getRy() {
		return ry;
	}
	public void setRy(double ry) {
		this.ry = ry;
	}
	public double getRz() {
		return rz;
	}
	public void setRz(double rz) {
		this.rz = rz;
	}

}

