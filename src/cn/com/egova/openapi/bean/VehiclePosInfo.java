package cn.com.egova.openapi.bean;

import java.util.Date;

public class VehiclePosInfo {
	
	private String simCardNum;
	
	private Date reportTime;
	
	private Double longtitude ;
	
	private Double latitude;
	
	private Double speed;
	
	private Double angle = 0.0;
	
	private Double altitude = 0.0;
	
	private Integer onlineFlag;
	
	private Double coordinateX;
	
	private Double coordinateY;
	
	

	public Double getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(Double coordinateX) {
		this.coordinateX = coordinateX;
	}

	public Double getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(Double coordinateY) {
		this.coordinateY = coordinateY;
	}

	public String getSimCardNum() {
		return simCardNum;
	}

	public void setSimCardNum(String simCardNum) {
		this.simCardNum = simCardNum;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getAngle() {
		return angle;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public Integer getOnlineFlag() {
		return onlineFlag;
	}

	public void setOnlineFlag(Integer onlineFlag) {
		this.onlineFlag = onlineFlag;
	}
	
	

}
