package cn.com.egova.openapi.base.helper;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

public class Security {
    private static final Logger LOGGER = LoggerFactory
            .getLogger("protocol");

    //加密
    public static String encrypt(String key, String content) {
        try {
            Cipher ecip;
            String ss = DigestUtils.md5DigestAsHex(key.getBytes()).toUpperCase();
            ss = ss.substring(0, 8);
            byte[] bytes = ss.getBytes();
            DESKeySpec ks = new DESKeySpec(bytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey sk = skf.generateSecret(ks);
            IvParameterSpec iv2 = new IvParameterSpec(bytes);

            ecip = Cipher.getInstance("DES/CBC/PKCS5Padding");
            ecip.init(Cipher.ENCRYPT_MODE, sk, iv2);
            byte[] bytesR = ecip.doFinal(content.getBytes("utf-8"));
            return byte2hex(bytesR);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        for (byte b : bytes)
            hs.append(String.format("%1$02X", b));
        return hs.toString();
    }

    // 解密
    public static String decrypt(String key, String content) {
        try {
            Cipher dcip;
            String ss = DigestUtils.md5DigestAsHex(key.getBytes())
                    .toUpperCase();
            ss = ss.substring(0, 8);
            byte[] bytes = ss.getBytes();
            DESKeySpec ks = new DESKeySpec(bytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey sk = skf.generateSecret(ks);
            IvParameterSpec iv2 = new IvParameterSpec(bytes);

            dcip = Cipher.getInstance("DES/CBC/PKCS5Padding");
            dcip.init(Cipher.DECRYPT_MODE, sk, iv2);
            byte[] bytesR = hex2byte(content);
            bytesR = dcip.doFinal(bytesR);
            return new String(bytesR, "utf-8");
        } catch (Exception ex) {
            LOGGER.error("解密失败", ex);
            return "fasle";
        }

    }

    private static byte[] hex2byte(String content) {
        int l = content.length() >> 1;
        byte[] result = new byte[l];
        for (int i = 0; i < l; i++) {
            int j = i << 1;
            String s = content.substring(j, j + 2);
            result[i] = Integer.valueOf(s, 16).byteValue();
        }
        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Security aa = new Security();
        String CC = aa.decrypt("b552df1da2c14bc0b9207f2d772e2de9", "50140072E27D609F111BC322DFBD695A597DFDC7AF730C0CE8BA0053B2EDE86362B6F473F9BF1BB905BF2E2FD83FB8BBD21AC8366234FD0D1E5C04C1CDE3206740A411EB40B2B422C0AC252D0B4D23795785B49084EE34D9864365AF054AE7E9F30AD6FE80E09A4F");
        System.out.println(CC);
		/*Security security = new Security();
		String pwd = security.encrypt("29fbd53f637044f29b9efcfd770783eb", "0010,CLDW,lingtu,543542B5A7E30DA3,CLDWZD0001,13940048917");
		System.out.println(pwd);
		
		String data = security.decrypt("29fbd53f637044f29b9efcfd770783eb", "50BC3815884FF68AFF426262D7F2C4A7ED3D3A2ECADA72C9E02A4F94174EA19C3D18F32CF34DACEA2ADC630BA5DBD2C6272BB1CDD224FD7877411DA8FA2738AE");
		System.out.println(data);*/
    }

}
