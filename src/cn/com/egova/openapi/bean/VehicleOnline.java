package cn.com.egova.openapi.bean;

import java.io.Serializable;

public class VehicleOnline implements Serializable {

    private int vehicleID;
    private String simCardNum;
    private Integer onlineFlag;

    public String getSimCardNum() {
        return simCardNum;
    }

    public void setSimCardNum(String simCardNum) {
        this.simCardNum = simCardNum;
    }

    public Integer getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

}
