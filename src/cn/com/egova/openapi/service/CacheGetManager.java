package cn.com.egova.openapi.service;

import cn.com.egova.openapi.bean.VehicleOnline;

import java.util.List;


public interface CacheGetManager {

    VehicleOnline getVehicleByVehicleNo(String simCardNum);

    VehicleOnline updateVehicleOnlineFlag(String simCardNum, VehicleOnline vbi);

    void deleteVehicleByVehicleNo(String simCardNum);

    Boolean getVehiclePos(String simCardNum);

    List<VehicleOnline> getVehicleByVehicleList();


}
