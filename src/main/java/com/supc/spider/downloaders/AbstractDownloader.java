package com.supc.spider.downloaders;


import com.supc.spider.Downloader;
import com.supc.spider.support.http.HttpClientFactory;
import com.supc.spider.utils.Charsets;
import com.supc.spider.utils.HtmlUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/10/29
 */
public abstract class AbstractDownloader implements Downloader {

    final Logger logger = LoggerFactory.getLogger(AbstractDownloader.class);

    public AbstractDownloader() {
    }

    public List<String> requsetForContents(URL url, List<String> contents) {
        return contents;
    }

    public String httpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        return httpRequest(httpPost);
    }

    public String httpPost(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        try {
            //设置参数
            StringEntity stringEntity = new StringEntity(params, Charsets.UTF8);
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return httpRequest(httpPost);
    }

    public String httpPost(String url, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(url);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charsets.UTF8));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return httpRequest(httpPost);
    }

    public String httpGet(String url) {
//        logger.info("httpGet: "+url);
        HttpGet httpGet = new HttpGet(url);
        return httpRequest(httpGet);
    }

    public String httpRequest(HttpRequestBase httpRequest) {
        CloseableHttpClient httpClient = HttpClientFactory.getHttpClient();
        String html = "";
        try {
            HttpEntity httpEntity = httpClient.execute(httpRequest).getEntity();
            html = EntityUtils.toString(httpEntity, Charsets.UTF8);
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }
        return html;
    }

    public String httpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();

            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, Charsets.UTF8);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }
        return responseContent;
    }

    public String httpsGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return httpsGet(httpGet);
    }
}
