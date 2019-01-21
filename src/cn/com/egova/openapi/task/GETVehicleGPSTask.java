package cn.com.egova.openapi.task;

import cn.com.egova.openapi.service.VehicleOtherManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.egova.openapi.service.VehicleApiManager;

@Component
@PropertySource("/WEB-INF/classes/config.properties")
public class GETVehicleGPSTask {
	
	Logger logger = Logger.getLogger(GETVehicleGPSTask.class);
	
	@Autowired
	VehicleApiManager vehicleApiManager;

	@Autowired
	VehicleOtherManager vehicleOtherManager;
	
	@Scheduled(cron="0 0/5 * * * ?")
	public void synVehicleGps(){
		
		logger.info("开启定时任务，同步车载轨迹");
		vehicleApiManager.synVehicleGps();

	}


	@Scheduled(cron="0 0/1 * * * ?")
	public void synVehicleInfoGps(){

		logger.info("开启定时任务，同步车辆信息");
		vehicleOtherManager.saveOrUpdateVehicle();

	}
}
