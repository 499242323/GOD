package cn.com.egova.openapi.constant;

import java.util.List;

import cn.com.egova.openapi.base.helper.ObjRowMapper;
import cn.com.egova.openapi.bean.VehicleOnline;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.com.egova.openapi.base.helper.BeanUtils;
import cn.com.egova.openapi.base.helper.PropertiesUtils;





public class InitConst {
	
	
	static JdbcTemplate jdbcTemplate = (JdbcTemplate) BeanUtils.getBean("jdbcTemplate");
	
	static Logger logger = Logger.getLogger(InitConst.class);

	public static 	List<VehicleOnline> simList;

	public static void initConfig(){

		String sql = "select vehicle_id as vehicleID,sim_card_num as simCardNum from tc_vehicle_api where gps_flag=1";
		simList = jdbcTemplate.query(sql,new ObjRowMapper<VehicleOnline>(VehicleOnline.class));
		logger.info("初始化获得车辆"+simList.size()+"辆");

		String password = PropertiesUtils.getValue("config.properties", "password");
		String username = PropertiesUtils.getValue("config.properties", "username");
		String apiUrl = PropertiesUtils.getValue("config.properties", "api.url");
		String apiID= PropertiesUtils.getValue("config.properties", "api.id");
		String usageFlag = PropertiesUtils.getValue("config.properties", "usage.flag");
		String jtjfh = PropertiesUtils.getValue("config.properties", "jtjfh");
		String gpsFlag = PropertiesUtils.getValue("config.properties", "gps.flag");
		String secretKey= PropertiesUtils.getValue("config.properties", "secret.key");
		
		String coordType= PropertiesUtils.getValue("config.properties", "server.coord.convert.type");
		String coordParam= PropertiesUtils.getValue("config.properties", "server.coord.convert.param");
		
		String vehicleFlag= PropertiesUtils.getValue("config.properties", "vehicle.flag");
		String companyFlag= PropertiesUtils.getValue("config.properties", "company.flag");

		ConfigMap.add("password", password);
		ConfigMap.add("username", username);
		ConfigMap.add("apiUrl", apiUrl);
		ConfigMap.add("apiID", apiID);
		ConfigMap.add("usageFlag", usageFlag);
		ConfigMap.add("jtjfh", jtjfh);
		ConfigMap.add("gpsFlag", gpsFlag);
		ConfigMap.add("secretKey", secretKey);
		ConfigMap.add("coordType", coordType);
		ConfigMap.add("coordParam", coordParam);
		ConfigMap.add("vehicleFlag", vehicleFlag);
		ConfigMap.add("companyFlag", companyFlag);

		logger.info("password:"+password);
		logger.info("username:"+username);
		
		logger.info("apiUrl:"+apiUrl);
		logger.info("apiID:"+apiID);
		logger.info("usageFlag:"+usageFlag);
	}

}
