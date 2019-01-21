package cn.com.egova.openapi.coord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CoordConvertNull implements CoordConvert {
    private static double DX = 0;
    private static double DY = 0;
	private boolean isXYChange = false;
	Log logger = LogFactory.getLog(CoordConvertNull.class);
	
	public CoordConvertNull(String params) {
		if (params != null) {
			try {
				if (params.contains("#")) {
					String[] pams = params.split("#");
					DX = Double.parseDouble(pams[0].trim());
					DY = Double.parseDouble(pams[1].trim());
					isXYChange = pams[2].trim().equalsIgnoreCase("1");
				} else {
					isXYChange = params.trim().equalsIgnoreCase("1");
				}
			} catch (Exception e) {
				logger.error("params="+params+" error="+e.getMessage());
			} 
		}else{
			logger.debug("params is null");
		} 
		logger.debug( "DX="+DX+",DY="+DY+",isXYChange="+isXYChange);
	}

	@Override
	public double[] convert(double latitude, double longitude) {
        double[] result = new double[2];
        result[0] = latitude + DX;
        result[1] = longitude + DY;
		return isXYChange ? new double[]{result[0],result[1]}:new double[]{result[1], result[0]};
	}

}
