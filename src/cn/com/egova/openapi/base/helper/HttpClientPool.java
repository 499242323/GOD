package cn.com.egova.openapi.base.helper;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

/**
 * httpclient
 *
 * @author wushuai
 */
public class HttpClientPool {

    private Logger logger = Logger.getLogger(HttpClientPool.class);

    private static PoolingHttpClientConnectionManager cm = null;
    private static RequestConfig requestConfig = null;

    /*最大连接数，应该放在配置文件内*/
    private static int maxTotal = 15;
    /*每个路由的最大连接数*/
    private static int maxPerRoute = 15;
    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     */
    private static int SERVER_REQUEST_TIME_OUT = 15000;
    /**
     * 服务端响应超时时间设置(单位:毫秒)
     */
    private static int SERVER_RESPONSE_TIME_OUT = 15000;

    /*从连接池获取连接的超时时间*/
    private static int CONNECTION_REQUEST_TIME_OUT = 2000;
    private static CloseableHttpClient client = null;

    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(SERVER_REQUEST_TIME_OUT)
                .setConnectTimeout(SERVER_RESPONSE_TIME_OUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT)
                .build();

        client = HttpClients.custom()
                .setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
    }

    public synchronized static CloseableHttpClient getHttpClient() {
        if (client == null) {
            client = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
        }
        return client;
    }



}