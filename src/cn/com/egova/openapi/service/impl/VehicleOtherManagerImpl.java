package cn.com.egova.openapi.service.impl;

import cn.com.egova.openapi.base.helper.EncrypGamma;
import cn.com.egova.openapi.base.helper.HttpClientPoolUtils;
import cn.com.egova.openapi.base.helper.Security;
import cn.com.egova.openapi.bean.VehicleOnline;
import cn.com.egova.openapi.constant.ConfigMap;
import cn.com.egova.openapi.constant.InitConst;
import cn.com.egova.openapi.service.VehicleOtherManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleOtherManagerImpl implements VehicleOtherManager {

    Logger logger = Logger.getLogger(VehicleOtherManagerImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public void saveOrUpdateVehicleApi() {


    }


    @Override
    public void saveOrUpdateVehicle() {
        List<VehicleOnline> list = InitConst.simList;
        for (VehicleOnline vehicleInfo : list) {
            this.getVehicleManInfo(vehicleInfo.getSimCardNum());
            this.getVehicleOtherInfo(vehicleInfo.getSimCardNum());
        }
    }


    private void getVehicleOtherInfo(String simCardNum) {

        String url = ConfigMap.get("apiUrl");
        String vehicleFlag = ConfigMap.get("companyFlag");
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
        StringBuilder stringBuilder = new StringBuilder(vehicleFlag)
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
        logger.info(url);
        String info;
        try {
            info = HttpClientPoolUtils.doGet(url);

            logger.info("other:" + info);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getVehicleManInfo(String simCardNum) {

        String url = ConfigMap.get("apiUrl");
        String vehicleFlag = ConfigMap.get("vehicleFlag");
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
        StringBuilder stringBuilder = new StringBuilder(vehicleFlag)
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

        logger.info(url);
        String info;
        try {
            info = HttpClientPoolUtils.doGet(url);

            logger.info("man:" + info);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTcVehicle(Object actDefId, String actDefName, Boolean process_state) {

        String updateSql = "update tc_vehicle set vehicle_num=?,vehicle_model=?,tonnage = ?,vehicle_color=? where vehicle_id = ?";

        jdbcTemplate.update(updateSql, process_state, actDefId, actDefName, this);
    }

    public void saveOrUpdateTcVehicle(Object actDefId, String actDefName, Boolean process_state) {

        String updateSql = "update tc_vehicle set vehicle_num=?,vehicle_model=?,tonnage = ?,vehicle_color=? where vehicle_id = ?";

        jdbcTemplate.update(updateSql, process_state, actDefId, actDefName, this);
    }
}
