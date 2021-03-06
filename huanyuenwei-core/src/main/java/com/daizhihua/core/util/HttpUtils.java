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
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

@Slf4j
public class HttpUtils {

        private static CloseableHttpClient httpClient;
        private static PoolingHttpClientConnectionManager manager; // ??????????????????
        private static final int CONNECT_TIMEOUT = HttpClientConfig.getHttpConnectTimeout();// ????????????????????????????????????10s
        private static final int SOCKET_TIMEOUT = HttpClientConfig.getHttpSocketTimeout();
        private static final int MAX_CONN = HttpClientConfig.getHttpMaxPoolSize(); // ???????????????
        private static final int Max_PRE_ROUTE = HttpClientConfig.getHttpMaxPoolSize();
        private static final int MAX_ROUTE = HttpClientConfig.getHttpMaxPoolSize();

        /**
         * ???http????????????????????????
         *
         * @param httpRequestBase
         *            http??????
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
         * ??????host???port??????httpclient??????
         *
         * @param host
         *            ??????????????????
         * @param port
         *            ??????????????????
         * @return
         */
        public static CloseableHttpClient createHttpClient(String host, int port) {
            ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
            LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", plainSocketFactory).register("https", sslSocketFactory).build();
            manager = new PoolingHttpClientConnectionManager(registry);
            // ??????????????????
            manager.setMaxTotal(MAX_CONN); // ???????????????
            manager.setDefaultMaxPerRoute(Max_PRE_ROUTE); // ?????????????????????
            HttpHost httpHost = new HttpHost(host, port);
            manager.setMaxPerRoute(new HttpRoute(httpHost), MAX_ROUTE);
            // ???????????????,??????????????????
            HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                    if (i > 3) {
                        // ????????????3???,????????????
                        log.error("retry has more than 3 time, give up request");
                        return false;
                    }
                    if (e instanceof NoHttpResponseException) {
                        // ?????????????????????,?????????????????????????????????,????????????
                        log.error("receive no response from server, retry");
                        return true;
                    }
                    if (e instanceof SSLHandshakeException) {
                        // SSL????????????
                        log.error("SSL hand shake exception");
                        return false;
                    }
                    if (e instanceof InterruptedIOException) {
                        // ??????
                        log.error("InterruptedIOException");
                        return false;
                    }
                    if (e instanceof UnknownHostException) {
                        // ??????????????????
                        log.error("server host unknown");
                        return false;
                    }
                    if (e instanceof ConnectTimeoutException) {
                        // ????????????
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
                        // ???????????????????????????????????????
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
         * ???????????????
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

            // ???????????? ????????????????????????
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
    public static String imageToBase64(String path) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }


}