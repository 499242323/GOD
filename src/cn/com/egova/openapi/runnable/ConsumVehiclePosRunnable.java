package cn.com.egova.openapi.runnable;

import org.apache.log4j.Logger;

import cn.com.egova.openapi.base.helper.BeanUtils;
import cn.com.egova.openapi.bean.VehiclePosInfo;
import cn.com.egova.openapi.constant.GPSBlockQueue;
import cn.com.egova.openapi.service.VehiclePosManager;


public class ConsumVehiclePosRunnable implements Runnable {

    Logger logger = Logger.getLogger(ConsumVehiclePosRunnable.class);

    VehiclePosManager vehiclePosManager = (VehiclePosManager) BeanUtils.getBean("vehiclePosManagerImpl");

    @Override
    public void run() {
        handleMessage();
    }

    public void handleMessage() {

        while (true) {
            try {
                VehiclePosInfo vpi = GPSBlockQueue.take();
                if (vpi != null) {
                    vehiclePosManager.handleMsg(vpi);
                } else {
                    logger.info("list--vpi null");
                }
            } catch (Exception e) {
                logger.error("one error occur", e);
            }
        }
    }

}
