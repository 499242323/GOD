package cn.com.egova.openapi.service;

import cn.com.egova.openapi.bean.VehicleOnline;
import cn.com.egova.openapi.bean.VehiclePosInfo;

public interface VehiclePosManager {

    void insertVehicleHistoryPos(VehiclePosInfo vpi);

    void saveVehiclePos(VehiclePosInfo vpi);

    boolean isVehicleExist(String simCardNum);

    void handleMsg(VehiclePosInfo vpi);

    void updateVehiclePos(VehiclePosInfo rli);

    void updateOnlineFlag(VehicleOnline vbi);

}
