package cn.com.egova.openapi.coord;

public class CoordConvertFactory {
    public static CoordConvert getConvert(String type,String parms) {
       
    	if (("7BJ54").equalsIgnoreCase(type)) {
            return new CoordConvert7Params(CoordConst.CoordType_BJ54,parms);  
        }else if (("7XA80").equalsIgnoreCase(type)) {
            return new CoordConvert7Params(CoordConst.CoordType_C80,parms); 
        }else if (("7WGS84").equalsIgnoreCase(type)) {
            return new CoordConvert7Params(CoordConst.CoordType_WGS84,parms);   
        }else if (("XA80").equalsIgnoreCase(type)) {
            return new CoordConvert4Params(CoordConvert4Params.COORDSYSTYPE_XIAN80,parms);  
        }else if (("WGS84").equalsIgnoreCase(type)) {
            return new CoordConvert4Params(CoordConvert4Params.COORDSYSTYPE_WGS84,parms);  
        }else if("YDCL".equalsIgnoreCase(type)){
        	// 移动测量
        	return new YDCLCoordConvert(0, parms);
        }else if("NULL".equalsIgnoreCase(type)){
	        return new CoordConvertGPS(parms);
		}else if("GPS".equalsIgnoreCase(type)){
        	return new CoordConvertGPS(parms);
		}else if("MKT".equalsIgnoreCase(type)){
        	// 墨卡托(Mercator)坐标转换公式
        	return new MercatorCoordConvert();
		}else if("xy2ll".equalsIgnoreCase(type)){
			return new CoordXY2WGS84(parms);
		}else if("BD".equalsIgnoreCase(type)){
			return new BDCoordConvert(parms);
		}else if(type==null || type.equals("")){
			return new CoordConvertNoConvert();
		}
		/*else if("ll2bd".equalsIgnoreCase(type)){
			return new CoordConvertBaidu();
		}else if("ll2bdm".equalsIgnoreCase(type)){
			return new CoordConvertBaiduM(jdbcTemplate);
		}else if("bd2ll".equalsIgnoreCase(type)){
			return new  CoordBaidu2Lonlat(jdbcTemplate);
	    }else if("bd2xy".equalsIgnoreCase(type)){
			return new  CoordBaidu2XY(jdbcTemplate);
	    }else if("bdxy2ll".equalsIgnoreCase(type)){
			return new  CoordBdxy2Lonlat(jdbcTemplate);
	    }else if("bdxy2xy".equalsIgnoreCase(type)){
			return new  CoordBdxy2XY(jdbcTemplate);
	    }else if("ll2xy".equalsIgnoreCase(type)){
			return new  CoordLonlat2XY(jdbcTemplate);
	    }else if("xy2bd".equalsIgnoreCase(type)){
			return new  CoordXY2Baidu(jdbcTemplate);
	    }*/
	    else{
	    	return new CoordConvertNoConvert();
        } 
    }
}
