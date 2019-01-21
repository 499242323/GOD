package cn.com.egova.openapi.base.helper;

public class EncodeUtils {

    public static String stringToUnicode(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            //ֱ�ӻ�ȡ�ַ��unicode������
            byte[] bytes = s.getBytes("unicode");
            //Ȼ����byteת���ɶ�Ӧ��16���Ʊ�ʾ����
            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);
            }
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertUTF8ToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        try {
            s = s.toUpperCase();
            int total = s.length() / 2;
            //��ʶ�ֽڳ���
            int pos = 0;
            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {
                int start = i * 2;
                //���ַ�������Ϊ�ڶ�������ָ���Ļ����е��з������
                buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
                pos++;
            }
            //ͨ��ʹ��ָ�����ַ����ָ�����ֽ�������������һ���µ��ַ�
            //���ַ�ĳ������ַ�ĺ�����˿��ܲ�����������ĳ��ȡ�
            return new String(buffer, 0, pos, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

}
