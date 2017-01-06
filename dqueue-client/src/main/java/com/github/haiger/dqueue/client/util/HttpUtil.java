package com.github.haiger.dqueue.client.util;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.haiger.dqueue.client.exception.DQueueException;

public class HttpUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

    private static final int DEFAULT_SOCKET_TIMEOUT = 2000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 1000;
    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 1000;
    private static final int DEFAULT_MAX_PER_ROUTE_CONNECTIONS = 32;
    private static final int DEFAULT_MAX_CONNECTIONS = 64;

    private static CloseableHttpClient httpClient;
    private static PoolingHttpClientConnectionManager cm;

    static {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).build();
        cm = new PoolingHttpClientConnectionManager(registry);
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE_CONNECTIONS);
        cm.setMaxTotal(DEFAULT_MAX_CONNECTIONS);

        SocketConfig socketConfig = SocketConfig.custom().setSoReuseAddress(true).build();
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT).setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT).build();

        httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig).build();

        /**
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    LOG.error("httpClient close error at:{}", e);
                }
                cm.close();
            }
        });**/
    }

    public static String post(String uri, String params) throws DQueueException {
        HttpPost post = new HttpPost(uri);
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        post.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        String responseContent = null;
        try {
            response = httpClient.execute(post);
            int httpCode = response.getStatusLine().getStatusCode();
            if (httpCode != 200) {
                throw new DQueueException("Post to " + uri + " with params:" + params + " returned HTTP " + httpCode);
            }
            
            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (ClientProtocolException | ParseException e) {
            throw new DQueueException(e);
        } catch (IOException e) {
            throw new DQueueException(e);
        } finally {
            post.releaseConnection();
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOG.error("response close error at:{}", e);
                }
            }
        }
        return responseContent;
    }
    
    public static void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            LOG.error("httpClient close error at:{}", e);
        }
        cm.close();
    }
}
