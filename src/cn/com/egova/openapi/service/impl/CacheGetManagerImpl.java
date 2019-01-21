package cn.com.egova.openapi.service.impl;

import cn.com.egova.openapi.base.helper.ObjRowMapper;
import cn.com.egova.openapi.bean.SchemaConst;
import cn.com.egova.openapi.bean.VehicleOnline;
import cn.com.egova.openapi.bean.VehiclePosInfo;
import cn.com.egova.openapi.constant.VehiclePosMap;
import cn.com.egova.openapi.service.CacheGetManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service(value = "cacheGetManagerImpl")
public class CacheGetManagerImpl implements CacheGetManager {

    static Logger logger = Logger.getLogger(CacheGetManagerImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Cacheable(value = "cacheVehicleBase", key = "'VehiclefindByNo'+#simCardNum")
    public VehicleOnline getVehicleByVehicleNo(String simCardNum) {

        //logger.info("-------cache-vehicle-no---------");

        VehicleOnline vbi = null;
        String selectSql = "select vehicle_id as vehicleID,sim_card_num as simCardNum,online_flag "
                + "as onlineFlag  from " + SchemaConst.DLSYS_ + ""
                + "tc_vehicle where sim_card_num = ?";


        List<VehicleOnline> vbis = jdbcTemplate.query(selectSql, new ObjRowMapper<VehicleOnline>(VehicleOnline.class), simCardNum);
        if (vbis.size() > 0) {
            vbi = vbis.get(0);
        } else {
            return null;
        }
        return vbi;
    }

    @CachePut(value = "cacheVehicleBase", key = "'VehiclefindByNo'+#simCardNum")
    public VehicleOnline updateVehicleOnlineFlag(String simCardNum, VehicleOnline vbi) {

        return vbi;
    }

    @CacheEvict(value = "cacheVehicleBase", key = "'VehiclefindByNo'+#simCardNum")
    public void deleteVehicleByVehicleNo(String simCardNum) {
        // TODO Auto-generated method stub
        return;
    }

    @Override
    public Boolean getVehiclePos(String simCardNum) {

        if (VehiclePosMap.get(simCardNum)) {
            return true;
        } else {
            String selectSql = "select sim_card_num as simCardNum from " + SchemaConst.DLMIS_ + ""
                    + "to_vehicle_pos where sim_card_num = ?";
            List<VehiclePosInfo> vps = jdbcTemplate.query(selectSql, new ObjRowMapper<VehiclePosInfo>(VehiclePosInfo.class), simCardNum);
            if (vps.size() > 0) {
                VehiclePosMap.add(simCardNum, true);
                return true;
            }
        }

        return false;
    }

    public List<VehicleOnline> getVehicleByVehicleList() {

        String selectSql = "select vehicle_id as vehicleID,sim_card_num as simCardNum,online_flag "
                + "as onlineFlag  from " + SchemaConst.DLSYS_ + ""
                + "tc_vehicle ";

        List<VehicleOnline> vbis = jdbcTemplate.query(selectSql, new ObjRowMapper<VehicleOnline>(VehicleOnline.class));

        return vbis;
    }
}
