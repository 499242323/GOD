package cn.com.egova.openapi.base.helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientPoolUtils {

    private static Logger logger = Logger.getLogger(HttpClientPoolUtils.class);

    public static String sentHttpRequest(String url, String requestMethod, Map<String, String> paramsMap) {
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
        if ("post".equals(requestMethod)) {
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/json");
            for (Iterator<String> it = paramsMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                String value = paramsMap.get(key);
                formparams.add(new BasicNameValuePair(key, value));
            }
            return doRequest(httppost, null, formparams);
        } else if ("get".equals(requestMethod)) {
            HttpGet httppost = new HttpGet(url);
            for (Iterator<String> it = paramsMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                String value = paramsMap.get(key);
                formparams.add(new BasicNameValuePair(key, value));
            }
            return doRequest(null, httppost, formparams);
        }
        return "";
    }

    private static String doRequest(HttpPost httpPost, HttpGet httpGet, List<BasicNameValuePair> formparams) {

        try {
            CloseableHttpResponse response = null;
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(25000).setConnectTimeout(3000)
                    .build();
            if (null != httpPost) {
                uefEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(uefEntity);
                httpPost.setConfig(requestConfig);
                response = HttpClientPool.getHttpClient().execute(httpPost);
            } else {
                httpGet.setConfig(requestConfig);
                response = HttpClientPool.getHttpClient().execute(httpGet);
            }
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity, "UTF-8");
            if (null == str || "".equals(str)) {
                return "";
            } else {
                return str;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return "";
    }

    /**
     * 处理json格式的body post请求
     *
     * @return
     * @throws Exception
     * @throws ClientProtocolException
     */
    public static String sentPostJson(String postUrl, String jsonStr) throws ClientProtocolException, Exception {
        HttpPost post = new HttpPost(postUrl);
        post.setHeader("Content-Type", "application/json");
        post.addHeader("Accept", "application/json");
        post.addHeader("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.4.2; HM NOTE 1LTEW MIUI/KHICNBH21.0)");
        post.addHeader("Connection", "Keep-Alive");
        post.addHeader("Accept-Encoding", "gzip");

        StringEntity s = new StringEntity(jsonStr, "utf-8");
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        post.setEntity(s);

        CloseableHttpResponse response = SslUtil.SslHttpClientBuild().execute(post);

        //执行请求操作，并拿到结果（同步阻塞）
        String body = null;
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }

    /**
     * 执行get方法
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {

        CloseableHttpResponse response = null;
        String body = null;
        try {
            HttpGet get = new HttpGet(url);

            response = HttpClientPool.getHttpClient().execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                body = EntityUtils.toString(response.getEntity());
            } else {
                logger.info("statusCode " + statusCode);
            }
        } catch (Exception e) {
            logger.error("请求异常" + e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }


    public static String postFileMultiPart(String url, Map<String, ContentBody> reqParam) throws IOException {
        CloseableHttpClient httpclient = SslUtil.SslHttpClientBuild();
        try {
            // 创建HttpPost
            HttpPost httppost = new HttpPost(url);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (Map.Entry<String, ContentBody> param : reqParam.entrySet()) {
                multipartEntityBuilder.addPart(param.getKey(), param.getValue());
            }
            HttpEntity reqEntity = multipartEntityBuilder.build();
            httppost.setEntity(reqEntity);
            // 执行post请求.
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                if (entity != null) {
                    return EntityUtils.toString(entity, Charset.forName("UTF-8"));
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            logger.error("uploadFile 请求出错..", e);
        }
        return null;
    }

}