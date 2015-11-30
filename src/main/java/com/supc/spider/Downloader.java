package com.supc.spider;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by caisupeng on 15/10/15.
 */
public interface Downloader {

    /**
     * post请求,传入url
     *
     * @param url 地址
     */
    String httpPost(String url);

    /**
     * post请求,传入url及String参数
     *
     * @param url    地址
     * @param params 参数(格式:key1=value1&key2=value2)
     */
    String httpPost(String url, String params);

    /**
     * post请求,传入url及ap参数
     *
     * @param url  地址
     * @param maps 参数
     */
    String httpPost(String url, Map<String, String> maps);

    /**
     * get请求,传入url
     *
     * @param url
     */
    String httpGet(String url);

    /**
     * Get或Post请求,返回String
     *
     * @param httpRequest
     * @return
     */
    String httpRequest(HttpRequestBase httpRequest);

    /**
     * 发送Https Get请求
     *
     * @param httpGet
     * @return
     */
    String httpsGet(HttpGet httpGet);

    /**
     * 发送Https get请求
     *
     * @param url
     */
    String httpsGet(String url);
}
