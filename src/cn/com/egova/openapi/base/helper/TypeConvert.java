package cn.com.egova.openapi.base.helper;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 从即时通讯基础包移植
 */
public class TypeConvert {
    private static final String TAG = "[ViewInitHelper]";
    private static final SimpleDateFormat DefaultFormat;
    private static final Pattern NumPattern;

    public TypeConvert() {
    }

    public static short parseShort(String str, short defaultValue) {
        boolean iResult = true;

        short iResult1;
        try {
            iResult1 = (short) parseInt((String) str, defaultValue);
        } catch (Exception var4) {
            iResult1 = defaultValue;
        }

        return iResult1;
    }

    public static short parseShort(Object obj, short defaultValue) {
        boolean iResult = true;

        short iResult1;
        try {
            iResult1 = parseShort(obj.toString(), defaultValue);
        } catch (Exception var4) {
            iResult1 = defaultValue;
        }

        return iResult1;
    }

    public static int parseInt(String str, int defaultValue) {
        boolean iResult = true;

        int iResult1;
        try {
            iResult1 = (int) parseDouble(str, (double) defaultValue);
        } catch (Exception var4) {
            iResult1 = defaultValue;
        }

        return iResult1;
    }

    public static int parseInt(Object obj, int defaultValue) {
        boolean iResult = true;

        try {
            double e = parseDouble(obj, (double) defaultValue);
            return (int) e;
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public static double parseDouble(String str, double defaultValue) {
        double iResult = -1.0D;

        try {
            return Double.parseDouble(str);
        } catch (Exception var6) {
            return defaultValue;
        }
    }

    public static double parseDouble(Object obj, double defaultValue) {
        double iResult = -1.0D;

        try {
            iResult = parseDouble(obj.toString(), defaultValue);
        } catch (Exception var6) {
            iResult = defaultValue;
        }

        return iResult;
    }

    public static float parseFloat(String str, float defaultValue) {
        float iResult = -1.0F;

        try {
            iResult = Float.parseFloat(str);
        } catch (Exception var4) {
            iResult = defaultValue;
        }

        return iResult;
    }

    public static float parseFloat(Object obj, float defaultValue) {
        float iResult = -1.0F;

        try {
            iResult = parseFloat(obj.toString(), defaultValue);
        } catch (Exception var4) {
            iResult = defaultValue;
        }

        return iResult;
    }

    public static long parseLong(String str, long defaultValue) {
        long iResult = -1L;

        try {
            iResult = Long.parseLong(str);
        } catch (Exception var6) {
            iResult = defaultValue;
        }

        return iResult;
    }

    public static long parseLong(Object obj, long defaultValue) {
        long iResult = -1L;

        try {
            iResult = parseLong(obj.toString(), defaultValue);
        } catch (Exception var6) {
            iResult = defaultValue;
        }

        return iResult;
    }

    public static String parseString(Object obj) {
        return parseString(obj, (String) null);
    }

    public static String parseString(Object obj, String defaultValue) {
        String strResult = "";

        try {
            strResult = obj.toString();
        } catch (Exception var4) {
            strResult = defaultValue;
        }

        return strResult;
    }

    public static String[] split(String str, String expr) {
        String[] result = null;
        if (str != null && !"".equals(str)) {
            try {
                result = str.split(expr);
            } catch (Exception var4) {
                result = null;
            }
        }

        return result;
    }

    public static boolean isDecimal(String str) {
        return str != null && !"".equals(str) ? NumPattern.matcher(str).matches() : false;
    }

    public static String DateToString(Date date) {
        return DateToString(date, "");
    }

    public static String DateToString(Long date, String defaultValue) {
        return DateToString(new Date(date.longValue()), defaultValue);
    }

    public static String DateToString(Long date) {
        return DateToString(new Date(date.longValue()));
    }

    public static String DateToString(Date date, String defaultValue) {
        try {
            return DateToString(date, defaultValue, DefaultFormat);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static String DateToString(Date date, String defaultValue, SimpleDateFormat simpleDateFormat) {
        try {
            return simpleDateFormat.format(date);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

//    public static String DateToShortStr(Date date) {
//        return DateUtils.dateTimeToStr(date);
//    }
//
//    public static String DateToHourStr(Date date) {
//        return DateUtils.dateToFormatStr(date, "yyyy-MM-dd HH:mm");
//    }
//
//    public static String DateToYearStr(Date date) {
//        return DateUtils.dateToFormatStr(date, "yyyy");
//    }

    public static Date StringToDate(String strDate) {
        return StringToDate(strDate, (Date) null);
    }

    public static Date StringToDate(String strDate, Date defaultValue) {
        try {
            return StringToDate(strDate, defaultValue, DefaultFormat);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static Date StringToDate(String strDate, Date defaultValue, SimpleDateFormat dateFormat) {
        try {
            return dateFormat.parse(strDate);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public static String StringListToSplitString(List<String> oriString, String splitStr) {
        if (oriString != null && oriString.size() != 0) {
            StringBuilder sb = new StringBuilder();
            Iterator i$ = oriString.iterator();

            while (i$.hasNext()) {
                String string = (String) i$.next();
                sb.append(string + splitStr);
            }

            sb.setLength(sb.length() - splitStr.length());
            return sb.toString();
        } else {
            return "";
        }
    }

    static {
        DefaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        NumPattern = Pattern.compile("[+-]?[0-9]*(\\.?)[0-9]*(\\%?)");
    }
}
