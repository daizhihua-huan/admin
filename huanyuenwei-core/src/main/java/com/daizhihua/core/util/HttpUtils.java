package com.daizhihua.core.util;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.daizhihua.core.config.HttpClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

@Slf4j
public class HttpUtils {

        private static CloseableHttpClient httpClient;
        private static PoolingHttpClientConnectionManager manager; // 连接池管理类
        private static final int CONNECT_TIMEOUT = HttpClientConfig.getHttpConnectTimeout();// 设置连接建立的超时时间为10s
        private static final int SOCKET_TIMEOUT = HttpClientConfig.getHttpSocketTimeout();
        private static final int MAX_CONN = HttpClientConfig.getHttpMaxPoolSize(); // 最大连接数
        private static final int Max_PRE_ROUTE = HttpClientConfig.getHttpMaxPoolSize();
        private static final int MAX_ROUTE = HttpClientConfig.getHttpMaxPoolSize();

        /**
         * 对http请求进行基本设置
         *
         * @param httpRequestBase
         *            http请求
         */
        private static void setRequestConfig(HttpRequestBase httpRequestBase) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECT_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            httpRequestBase.setConfig(requestConfig);
        }

        public static CloseableHttpClient getHttpClient(String url) {
            String hostName = url.split("/")[2];
            // System.out.println(hostName);
            int port = 80;
            if (hostName.contains(":")) {
                String[] args = hostName.split(":");
                hostName = args[0];
                port = Integer.parseInt(args[1]);
            }
            httpClient = createHttpClient(hostName, port);
            return httpClient;
        }

        /**
         * 根据host和port构建httpclient实例
         *
         * @param host
         *            要访问的域名
         * @param port
         *            要访问的端口
         * @return
         */
        public static CloseableHttpClient createHttpClient(String host, int port) {
            ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
            LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", plainSocketFactory).register("https", sslSocketFactory).build();
            manager = new PoolingHttpClientConnectionManager(registry);
            // 设置连接参数
            manager.setMaxTotal(MAX_CONN); // 最大连接数
            manager.setDefaultMaxPerRoute(Max_PRE_ROUTE); // 路由最大连接数
            HttpHost httpHost = new HttpHost(host, port);
            manager.setMaxPerRoute(new HttpRoute(httpHost), MAX_ROUTE);
            // 请求失败时,进行请求重试
            HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                    if (i > 3) {
                        // 重试超过3次,放弃请求
                        log.error("retry has more than 3 time, give up request");
                        return false;
                    }
                    if (e instanceof NoHttpResponseException) {
                        // 服务器没有响应,可能是服务器断开了连接,应该重试
                        log.error("receive no response from server, retry");
                        return true;
                    }
                    if (e instanceof SSLHandshakeException) {
                        // SSL握手异常
                        log.error("SSL hand shake exception");
                        return false;
                    }
                    if (e instanceof InterruptedIOException) {
                        // 超时
                        log.error("InterruptedIOException");
                        return false;
                    }
                    if (e instanceof UnknownHostException) {
                        // 服务器不可达
                        log.error("server host unknown");
                        return false;
                    }
                    if (e instanceof ConnectTimeoutException) {
                        // 连接超时
                        log.error("Connection Time out");
                        return false;
                    }
                    if (e instanceof SSLException) {
                        log.error("SSLException");
                        return false;
                    }
                    HttpClientContext context = HttpClientContext.adapt(httpContext);
                    HttpRequest request = context.getRequest();
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        // 如果请求不是关闭连接的请求
                        return true;
                    }
                    return false;
                }
            };
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).setRetryHandler(handler)
                    .build();
            return client;
        }

        /**
         * 关闭连接池
         */
        public static void closeConnectionPool() {
            try {
                httpClient.close();
                manager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

  public static JSONObject doPostForm(String url, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        setRequestConfig(httpPost);
        String resultString = "";
        CloseableHttpResponse response = null;
        try {

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            if (params != null) {
                for (String key : params.keySet()) {

                    builder.addPart(key, new StringBody(params.get(key),
                            ContentType.create("text/plain", Consts.UTF_8)));


                }
            }
            HttpEntity reqEntity = builder.build();
            httpPost.setEntity(reqEntity);

            // 发起请求 并返回请求的响应
            response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject(resultString);
    }

    private static   byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
//
//    public static void main(String[] args) {
//        Map<String, String> params = new HashMap<>();
//        params.put("appKey","7a3d2345e1e049b59e5e11e79e82f7b5");
//        params.put("appSecret","0cb247064bc4299f5b114f5d92bfea31");
//        JSONObject jsonObject = doPostForm("https://open.ys7.com/api/lapp/token/get", params);
//        System.out.println(jsonObject);
//    }
}