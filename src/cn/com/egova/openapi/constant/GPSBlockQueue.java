package cn.com.egova.openapi.constant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import cn.com.egova.openapi.bean.VehiclePosInfo;




public class GPSBlockQueue {
	
	static Logger logger = Logger.getLogger(GPSBlockQueue.class);
	
	public static BlockingQueue<VehiclePosInfo> queue = new LinkedBlockingQueue<VehiclePosInfo>(10000);
	
	public static void put(VehiclePosInfo vpi) {
		
		try {
			logger.debug("put vpi to queue");
			queue.offer(vpi, 5, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("轨迹信息插入队列出错",e);
		} 
	}

	
	public static VehiclePosInfo take() {
		VehiclePosInfo vpi = null;
		try {
			
			logger.debug("take vpi from queue");
			vpi = queue.take();
		} catch (InterruptedException e) {
			logger.error("从队列取出轨迹信息出错",e);
		}
		return vpi;
	}

}
