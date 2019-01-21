package cn.com.egova.openapi.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.egova.openapi.service.OnlineFlagManager;

@Component
public class UpdateOnlineFlagTask {

    Logger logger = Logger.getLogger(UpdateOnlineFlagTask.class);

    @Autowired
    OnlineFlagManager onlineFlagManager;


    @Scheduled(cron = "0 0/2 * * * ?")
    public void saveVehiclePos() {

        logger.info("开启定时任务，更新车辆在线状态");
        onlineFlagManager.updateOnlineFlag();
    }

}
