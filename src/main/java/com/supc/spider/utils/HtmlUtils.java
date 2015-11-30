/*
 * Project:  msc-service
 * Module:   msc-core
 * File:     HttpUtils.java
 * Modifier: xyang
 * Modified: 2015-05-07 16:42
 *
 * Copyright (c) 2014 Wisorg All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.supc.spider.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:xyang@wisorg.com">xyang</a>
 * @version V1.0, 15/5/7
 */
public class HtmlUtils {

    public static final String HTML_TAG_A = "a";
    public static final String HTML_TAG_IMG = "img";
    public static final String HTML_TAG_P = "p";
    public static final String HTML_TAG_EMBED = "embed";

    public static final String TAG_ATTRIBUTE_HREF = "href";
    public static final String DOMAIN_HTTP = "http://";
    public static final Pattern patternDomain = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
    public static final Pattern patternHttp = Pattern.compile("^http\\:\\/\\/.*", Pattern.CASE_INSENSITIVE);
    public static final Pattern patternHtmlTag = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);

    public static String getDomainByUrl(String url) {

        Matcher matcher = patternDomain.matcher(url);
        return matcher.find() ? matcher.group() : "";
    }

    public static String validateUrl(String url,String domain) {

         if (StringUtils.isEmpty(url)) return "";

        StringBuilder sb = new StringBuilder();
        Matcher mh = patternHttp.matcher(url);
        Matcher md = patternDomain.matcher(url);

        if (!mh.find())
            sb.append(DOMAIN_HTTP);

        if (!md.find()) {
            sb.append(domain);
            if (url.charAt(0) != '/')
                sb.append('/');
            sb.append(url);
        } else {
            sb.append(url);
        }
        return sb.toString();

    }

    public static String htmlTagFilter(String str) {
        Matcher m = patternHtmlTag.matcher(str);
        return m.replaceAll(StringUtils.EMPTY);
    }
}
