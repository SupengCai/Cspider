package com.supc.spider.support.http;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpClientFactory {

    private static HttpClientBuilder builder;
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 300;
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;
    public static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 30 * 1000;

    public static int maxTotalConnections = DEFAULT_MAX_TOTAL_CONNECTIONS;
    public static int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;
    public static int soTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;
    public static int connectionTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;
    public static boolean disableCookie = true;
    public static Map<String, String> defaultHeaders;

    private static void createHttpClientBuilder() {
        if (builder == null) {
            builder = HttpClientBuilder.create();
        }
        builder.setMaxConnTotal(maxTotalConnections);
        builder.setMaxConnPerRoute(maxConnectionsPerRoute);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(soTimeout).setConnectTimeout(connectionTimeout).build();
        builder.setDefaultRequestConfig(requestConfig);

        if (disableCookie) {
            builder.disableCookieManagement();
        }

        if (!CollectionUtils.isEmpty(defaultHeaders)) {
            List<Header> headers = new ArrayList<>();
            for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
                BasicHeader header = new BasicHeader(entry.getKey(), entry.getValue());
                headers.add(header);
            }
            builder.setDefaultHeaders(headers);
        } else {
            initDefaultHeaders();
        }
    }

    public static synchronized CloseableHttpClient getHttpClient() {
        if (builder == null) createHttpClientBuilder();
        CloseableHttpClient httpClient = builder.build();
        return httpClient;
    }

    private static void initDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Charset", "utf-8;q=0.7,*;q=0.7");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-cn,zh;q=0.5");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        headers.put("Connection", "keep-alive");
        headers.put("Host", "www.baidu.com");
//            headers.put("Cookie", "__utma=226521935.73826752.1323672782.1325068020.1328770420.6;");
//            headers.put("refer", "http://www.baidu.com/s?tn=monline_5_dg&bs=httpclient4+MultiThreadedHttpConnectionManager");
        defaultHeaders = headers;
    }

}
