package cn.com.egova.openapi.coord;

import java.math.BigDecimal;

public class CoordConvertNoConvert implements CoordConvert {

	public double[] convert(double latitude, double longitude) {
		double xy[] = {longitude,latitude};
		return xy;
	}

}
