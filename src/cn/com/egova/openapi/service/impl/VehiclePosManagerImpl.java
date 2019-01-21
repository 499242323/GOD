package cn.com.egova.openapi.service.impl;


import cn.com.egova.openapi.bean.SchemaConst;
import cn.com.egova.openapi.bean.VehicleOnline;
import cn.com.egova.openapi.bean.VehiclePosInfo;
import cn.com.egova.openapi.constant.CoordConst;
import cn.com.egova.openapi.constant.OnlineFlagMap;
import cn.com.egova.openapi.coord.CoordConvert;
import cn.com.egova.openapi.service.CacheGetManager;
import cn.com.egova.openapi.service.VehiclePosManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VehiclePosManagerImpl implements VehiclePosManager {

    Logger logger = Logger.getLogger(VehiclePosManagerImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CacheGetManager cacheGetManager;


    @Override
    public void handleMsg(VehiclePosInfo vpi) {
        try {

            String simCardNum = vpi.getSimCardNum();
            VehicleOnline vi = cacheGetManager.getVehicleByVehicleNo(simCardNum);

            if (vi == null) {
                logger.error("this car not register:" + simCardNum);
                cacheGetManager.deleteVehicleByVehicleNo(simCardNum);
            } else {

                //转换坐标
                double latitude = vpi.getLatitude();
                double longitude = vpi.getLongtitude();

                CoordConvert coordConvert = CoordConst.getCoordConvertor();
                double[] xy = coordConvert.convert(latitude, longitude);
                vpi.setCoordinateX(xy[0]);
                vpi.setCoordinateY(xy[1]);

                if (cacheGetManager.getVehiclePos(vpi.getSimCardNum())) {
                    this.updateVehiclePos(vpi);
                } else {
                    this.saveVehiclePos(vpi);
                }

                this.insertVehicleHistoryPos(vpi);

                //更新在线状态
                Date recordTime = vpi.getReportTime();
                if (simCardNum != null && recordTime != null) {
                    OnlineFlagMap.add(simCardNum, recordTime);
                }
            }
        } catch (Exception e) {
            logger.error("GPS数据转换入库失败", e);
        }
    }

    @Override
    public void insertVehicleHistoryPos(VehiclePosInfo vpi) {
        String insertSql = "insert into " + SchemaConst.DLMIS_ + "to_vehicle_pos_history(sim_card_num,angle,coordinate_x,coordinate_y,latitude,longitude,"
                + "record_time,speed,upload_time,altitude) "
                + "values(?,?,?,?,?,?,?,?,?,?)";
        try {
            jdbcTemplate.update(insertSql, new Object[]{
                    vpi.getSimCardNum(),
                    vpi.getAngle(),
                    vpi.getCoordinateX(),
                    vpi.getCoordinateY(),
                    vpi.getLatitude(),
                    vpi.getLongtitude(),
                    vpi.getReportTime(),
                    vpi.getSpeed(),
                    new Date(),
                    vpi.getAltitude()

            });
            logger.info("[sucess]save vehicle pos history:" + vpi.getSimCardNum());
        } catch (Exception e) {
            logger.error("[error]insert vehicle pos history table error:", e);
        }

    }


    @Override
    public void saveVehiclePos(VehiclePosInfo vpi) {

        String insertSql = "insert into " + SchemaConst.DLMIS_ + "to_vehicle_pos(sim_card_num,angle,coordinate_x,coordinate_y,latitude,longitude,"
                + "record_time,speed,upload_time,altitude) "
                + "values(?,?,?,?,?,?,?,?,?,?)";
        try {
            jdbcTemplate.update(insertSql, new Object[]{
                    vpi.getSimCardNum(),
                    vpi.getAngle(),
                    vpi.getCoordinateX(),
                    vpi.getCoordinateY(),
                    vpi.getLatitude(),
                    vpi.getLongtitude(),
                    vpi.getReportTime(),
                    vpi.getSpeed(),
                    new Date(),
                    vpi.getAltitude()


            });
            logger.info("[sucess]save vehicle pos :" + vpi.getSimCardNum());
        } catch (Exception e) {
            logger.error("[error]insert vehicle pos table error:", e);
        }

    }

    @Override
    public void updateVehiclePos(VehiclePosInfo rli) {

        String updateSql = "update " + SchemaConst.DLMIS_ + "to_vehicle_pos set angle=?,coordinate_x=?,coordinate_y=?,"
                + "latitude=?,longitude=?,record_time=?,speed=?,upload_time=?,altitude=? where sim_card_num = ?";
        try {
            jdbcTemplate.update(updateSql, new Object[]{

                    rli.getAngle(),
                    rli.getCoordinateX(),
                    rli.getCoordinateY(),
                    rli.getLatitude(),
                    rli.getLongtitude(),
                    rli.getReportTime(),
                    rli.getSpeed(),
                    new Date(),
                    rli.getAltitude(),
                    rli.getSimCardNum()

            });

            logger.info("[sucess]update vehicle pos:" + rli.getSimCardNum());
        } catch (Exception e) {
            logger.error("[error]insert vehicle pos table error:", e);
        }

        return;
    }


    @Override
    public boolean isVehicleExist(String simCardNum) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void updateOnlineFlag(VehicleOnline vbi) {
        String updateSql = "update tc_vehicle set online_flag = ? where sim_card_num = ?";
        jdbcTemplate.update(updateSql, vbi.getOnlineFlag(), vbi.getSimCardNum());

    }

}
