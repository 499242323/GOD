package cn.com.egova.openapi.base.helper;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * BASE64的加密解密是双向的，可以求反解。
 * MD5、SHA以及HMAC是单向加密，任何数据加密后只会产生唯一的一个加密串，通常用来校验数据在传输过程中是否被修改。
 * 其中HMAC算法有一个密钥，增强了数据传输过程中的安全性，强化了算法外的不可控因素。
 * 单向加密的用途主要是为了校验数据在传输过程中是否被修改。
 */
public class EncrypGamma {

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * MAC算法可选以下多种算法
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     */

    public static final String KEY_MAC = "HmacMD5";

    //将字节数组转化为字符串
    protected static String convertbyte2String(byte[] byteResult) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        //4位代表一个16进制，所以长度需要变为原来2倍
        char[] result = new char[byteResult.length * 2];

        int index = 0;
        for (byte b : byteResult) {
            //先转换高4位
            result[index++] = hexDigits[(b >>> 4) & 0xf];
            result[index++] = hexDigits[b & 0xf];
        }
        return new String(result);
    }

    protected static Boolean assertArrayEquals(byte[] bytes, byte[] bytes1) {
        if (new String(bytes).equals(new String(bytes1))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Old BASE64解码
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }
    /**
     * old BASE64编码
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }
    /**
     * old BASE64解码
     */
//    public static String decodedByBase64(String str) {
//        if (str == null || str.length() <= 0) {
//            return null;
//        }
//        String decoded = new String(
//                Base64
//                        .getDecoder()
//                        .decode(str),
//                StandardCharsets.UTF_8);
//        return decoded;
//    }
    /**
     *  jdk1.8 Base64编码
     */
//    protected static String EncoderByBase64(String str) {
//        if (str == null || str.length() <= 0) {
//            return null;
//        }
//        String encoded = Base64
//                .getEncoder()
//                .encodeToString(str.getBytes(StandardCharsets.UTF_8));
//        return encoded;
//    }

    /**
     *  MD5加密
     *  message-digest algorithm 5 （信息-摘要算法）
     */
    public static byte[] EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
//        md5.update(str.getBytes("utf-8"));
//        return md5.digest(str.getBytes("utf-8"));
        return md5.digest(str.getBytes());
    }

    /**
     *   SHA(Secure Hash Algorithm，安全Hash散列算法）
     */
    public static byte[] EncoderBySHA(String str) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(str.getBytes("utf-8"));
        return sha.digest();
    }

    /**
     *  Mac 得到key
     *  经过BASE64编码得到Key
     */
    protected static String initHMACKey(String key_name) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(key_name);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }
    /**
     *  Mac加密
     *  Key先经过BASE64解码
     */
    public static byte[] encryptHMACKey(byte[] data, String key, String key_name) throws Exception {

        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), key_name);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);

    }


}