package cn.com.egova.openapi.base.helper;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SslUtil {
    /*最大连接数，应该放在配置文件内*/
    private static int maxTotal = 100;
    /*每个路由的最大连接数*/
    private static int maxPerRoute = 100;
    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     */
    private static int SERVER_REQUEST_TIME_OUT = 3000;
    /**
     * 服务端响应超时时间设置(单位:毫秒)
     */
    private static int SERVER_RESPONSE_TIME_OUT = 3000;

    /*从连接池获取连接的超时时间*/
    private static int CONNECTION_REQUEST_TIME_OUT = 3000;
    private static RequestConfig requestConfig = null;

    public static CloseableHttpClient SslHttpClientBuild() {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", trustAllHttpsCertificates()).build();
        //创建ConnectionManager，添加Connection配置信息
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(SERVER_REQUEST_TIME_OUT)
                .setConnectTimeout(SERVER_RESPONSE_TIME_OUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        return httpClient;
    }

    private static SSLConnectionSocketFactory trustAllHttpsCertificates() {
        SSLConnectionSocketFactory socketFactory = null;
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");//sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, null);
            socketFactory = new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);
            //HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return socketFactory;
    }

    static class miTM implements TrustManager, X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            //don't check
        }
    }

}
