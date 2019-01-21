package cn.com.egova.openapi.service.impl;


import cn.com.egova.openapi.base.helper.EncrypGamma;
import cn.com.egova.openapi.base.helper.HttpClientPoolUtils;
import cn.com.egova.openapi.base.helper.JsonUtils;
import cn.com.egova.openapi.base.helper.Security;
import cn.com.egova.openapi.bean.VehicleGpsListInfo;
import cn.com.egova.openapi.bean.VehicleGpsObj;
import cn.com.egova.openapi.bean.VehicleOnline;
import cn.com.egova.openapi.bean.VehiclePosInfo;
import cn.com.egova.openapi.constant.ConfigMap;
import cn.com.egova.openapi.constant.GPSBlockQueue;
import cn.com.egova.openapi.constant.InitConst;
import cn.com.egova.openapi.service.VehicleApiManager;
import cn.com.egova.openapi.service.VehicleOtherManager;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class VehicleApiManagerImpl implements VehicleApiManager {

    Logger logger = Logger.getLogger(VehicleApiManagerImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    VehicleOtherManager vehicleOtherManager;

    @Override
    public boolean synVehicleGps() {
        List<VehicleOnline> list = InitConst.simList;
        for (VehicleOnline vehicle : list) {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            VehicleGpsListInfo gpsInfo = this.getVehicleGpsListInfo(vehicle.getSimCardNum());
            if (gpsInfo == null) {
                continue;
            }
            if (gpsInfo.getC().equals("000")) {
                List<VehicleGpsObj> objs = gpsInfo.getDlist();
                for (VehicleGpsObj obj : objs) {
                    VehiclePosInfo vpi = this.parseVehiclePosInfo(gpsInfo.getSim(), obj);
                    //插入消息队列
                    GPSBlockQueue.put(vpi);
                }
            } else {
                logger.error("获取车载gps信息出错,rsp:" + gpsInfo.getC());
            }
        }
        return true;
    }

    private VehicleGpsListInfo getVehicleGpsListInfo(String simCardNum) {

        String url = ConfigMap.get("apiUrl");
        String gpsFlag = ConfigMap.get("gpsFlag");
        String username = ConfigMap.get("username");
        String password = ConfigMap.get("password");
        String usageFlag = ConfigMap.get("usageFlag");
        String jtjfh = ConfigMap.get("jtjfh");
        String secretKey = ConfigMap.get("secretKey");
        String apiID = ConfigMap.get("apiID");
        try {
            password = StringUtils.stripToEmpty(new String(EncrypGamma.encryptBASE64(EncrypGamma.EncoderByMd5(password))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder(gpsFlag)
                .append(",")
                .append(usageFlag)
                .append(",")
                .append(username)
                .append(",")
                .append(password)
                .append(",")
                .append(jtjfh)
                .append(",")
                .append(simCardNum);
        String strData = stringBuilder.toString();

        String strParam = Security.encrypt(secretKey, strData);
        url = url + apiID + "/" + strParam;
        String strRsp = null;
        VehicleGpsListInfo vgli;
        try {
            strRsp = HttpClientPoolUtils.doGet(url);
        } catch (Exception e) {
            logger.error("http请求gps失败:", e);
        }
        if (strRsp != null) {
            JSONObject jsonObject = JSONObject.parseObject(strRsp);
            String strJson = Security.decrypt(secretKey, jsonObject.getString("data"));
            strJson = "{\"c\":\"" + jsonObject.getString("c") + "\",\"sim\":\"" + jsonObject.getString("sim") + "\",\"dlist\":" + strJson + "}";
            vgli = JsonUtils.fromJson(strJson, VehicleGpsListInfo.class);
        }else {
            return null;
        }
        return vgli;
    }

    private VehiclePosInfo parseVehiclePosInfo(String simCardNum, VehicleGpsObj obj) {

        VehiclePosInfo vpi = new VehiclePosInfo();
        vpi.setSimCardNum(simCardNum);

        String ctime = obj.getCtime();
        String angle = obj.getDir();
        String altitude = obj.getHeight();
        String lat = obj.getLat();
        String lon = obj.getLon();
        String speed = obj.getSpeed();
        String fstate = obj.getFstate();//是否点火

        Long ctimeT = Long.valueOf(ctime) * 1000;
        Date reportTime = new Date(ctimeT);

        Double ang = Double.valueOf(angle);
        Double alti = Double.valueOf(altitude);
        Double latitude = Double.valueOf(lat) / 3600000;
        Double longitude = Double.valueOf(lon) / 3600000;
        Double spe = Double.valueOf(speed);

        vpi.setReportTime(reportTime);
        vpi.setAltitude(alti);
        vpi.setAngle(ang);
        vpi.setLatitude(latitude);
        vpi.setLongtitude(longitude);
        vpi.setSpeed(spe);

        return vpi;

    }


}
