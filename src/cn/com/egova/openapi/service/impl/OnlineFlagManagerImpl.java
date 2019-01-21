package cn.com.egova.openapi.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.egova.openapi.bean.VehicleOnline;
import cn.com.egova.openapi.constant.OnlineFlagMap;
import cn.com.egova.openapi.service.CacheGetManager;
import cn.com.egova.openapi.service.OnlineFlagManager;
import cn.com.egova.openapi.service.VehiclePosManager;


@Service
public class OnlineFlagManagerImpl implements OnlineFlagManager {

    @Autowired
    CacheGetManager cacheGetManager;

    @Autowired
    VehiclePosManager vehiclePosManager;


    Logger logger = Logger.getLogger(OnlineFlagManagerImpl.class);

    @Override
    public boolean updateOnlineFlag() {

        Map<String, Date> onlineMap = OnlineFlagMap.map;

        for (String simCardNum : onlineMap.keySet()) {
            try {
                Integer tempOnlineFlag = 0;

                Date reportTime = onlineMap.get(simCardNum);
                if (reportTime != null) {
                    Date pos = reportTime;
                    Date now = new Date();
                    long timeGap = now.getTime() - pos.getTime();
                    VehicleOnline vbi = cacheGetManager.getVehicleByVehicleNo(simCardNum);
                    if (vbi == null) {
                        cacheGetManager.deleteVehicleByVehicleNo(simCardNum);
                    } else {
                        tempOnlineFlag = (timeGap < 600000) ? 1 : 0;// 10分钟(60000毫秒)间隔以上为车辆离线
                        //logger.info("vehicleNo:"+vehicleNo+"-tempOnlieFlag:"+tempOnlineFlag);
                        if (vbi.getOnlineFlag() != tempOnlineFlag) {
                            vbi.setOnlineFlag(tempOnlineFlag);
//                            cacheGetManager.updateVehicleOnlineFlag(simCardNum, vbi);
                            if (vbi.getOnlineFlag() != null) {
                                vehiclePosManager.updateOnlineFlag(vbi);
                            } else {
                                logger.info("vehicleNo:" + simCardNum + "-tempOnlieFlag:" + tempOnlineFlag);
                            }

                        }
                    }
                } else {
                    VehicleOnline vbi = cacheGetManager.getVehicleByVehicleNo(simCardNum);
                    if (vbi == null) {
                        cacheGetManager.deleteVehicleByVehicleNo(simCardNum);
                    } else {
                        vbi.setOnlineFlag(0);
//                        cacheGetManager.updateVehicleOnlineFlag(simCardNum, vbi);
                        if (vbi.getOnlineFlag() != null) {
                            vehiclePosManager.updateOnlineFlag(vbi);
                        } else {
                            logger.info("vehicleNo:" + simCardNum + "-tempOnlieFlag:" + tempOnlineFlag);
                        }

                    }

                }
            } catch (Exception e) {
                logger.error("更新在线状态出错" + simCardNum, e);
            }

        }

        return true;
    }

}
