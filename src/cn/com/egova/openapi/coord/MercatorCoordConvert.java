package cn.com.egova.openapi.coord;

public class MercatorCoordConvert implements CoordConvert {

	@Override
	public double[] convert(double latitude, double longitude) {
		return doTrans(longitude, latitude);
	}
	
    private static double[] lonLat2Mercator(double lon, double lat) {
        double[] xy = new double[2];
        double x = lon * 20037508.342789 / 180;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34789 / 180;
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    private double[] doTrans(double l, double b) {
        try {
            double[] result = new double[2];
            result = lonLat2Mercator(l, b);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private double[] doTrans(double l, double b, double z) {
        try {
            double[] result = new double[2];
            result = lonLat2Mercator(l, b);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
