package cn.com.egova.openapi.base.helper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件工具类
 *
 * @author yindl
 */
public class PropertiesUtils {

    /**
     * 获取属性文件中的键值
     *
     * @param FileName
     * @param key
     * @return 属性文件中的键值
     */
    public static String getValue(String FileName, String key) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(FileName);
//			String filePath = System.getProperty("user.dir") + "/conf/config.properties";
//			InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

}
