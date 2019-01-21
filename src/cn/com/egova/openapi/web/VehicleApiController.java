package cn.com.egova.openapi.web;

import cn.com.egova.openapi.bean.ResultInfo;
import cn.com.egova.openapi.service.VehicleOtherManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="api/")
public class VehicleApiController {
	
	@Autowired
	VehicleOtherManager vehicleOtherManager;
	
	@RequestMapping(value="getinfo")
	@ResponseBody
	public ResultInfo getVehicleInfo(HttpServletRequest request, HttpServletResponse response){
		vehicleOtherManager.saveOrUpdateVehicle();
		return new ResultInfo(true,1,"SUCCESS");
	}

}
