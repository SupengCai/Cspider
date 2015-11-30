package com.supc.spider.utils;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/23
 */
public class httpUtils {
//        public static String getUserAgent(HttpRequest request) {
//            return StringUtils.defaultString(HtmlUtils.getHeader(request, "user-agent")).toLowerCase();
//        }
//
//    public static boolean isIos(Http.Request request) {
//        return StringUtils.containsAny(getUserAgent(request), "iphone", "ipad");
//    }
//
//    public static boolean isAndroid(Http.Request request) {
//        return getUserAgent(request).contains("android");
//    }
//
//    public static ClientType getClientType(Http.Request request) {
//        if (request == null) {
//            request = Http.Request.current();
//        }
//        if (request != null) {
//            String userAgent = getUserAgent(request);
//            if (userAgent.lastIndexOf("mah/") > -1 || userAgent.lastIndexOf("thttpclient") > -1) {//old client UA
//                return ClientType.APP;
//            } else if (userAgent.lastIndexOf("msc/") > -1) {
//                return ClientType.APPBROWSER;
//            } else if (userAgent.lastIndexOf("msb/") > -1) {
//                return ClientType.APPBBROWSER;
//            } else if (userAgent.lastIndexOf("msa/") > -1) {
//                return ClientType.APPCROWSER;
//            } else if (userAgent.lastIndexOf("micromessenger") > -1) {
//                return ClientType.WEIXIN;
//            } else if (userAgent.lastIndexOf("alipayclient") > -1) {
//                return ClientType.ALIPAY;
//            } else if (userAgent.lastIndexOf("qq/") > -1) {
//                return ClientType.QQ;
//            } else if (userAgent.lastIndexOf("mbah/") > -1) {
//                return ClientType.APPB;
//            } else if (userAgent.lastIndexOf("mcah/") > -1) {
//                return ClientType.APPC;
//            } else {
//                return ClientType.BROWSER;
//            }
//        }
//        return ClientType.UNKNOWN;
//    }
//
//    public static int getAppVersion(Http.Request request) {
//        if (request == null) {
//            request = Http.Request.current();
//        }
//        if (request != null) {
//            String userAgent = getUserAgent(request);
//            int index = userAgent.lastIndexOf('/');
//            if (index > 0) {
//                return TextUtils.parseVersion(userAgent.substring(index + 1));
//            }
//        }
//        return Version.MAX;
//    }
//
//    public static final Set<String> PARAM_IGNORE = Sets.newHashSet("ctlPrefix", "method", "body");
//
//    public static String toString(Http.Request request) {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String[]> entry : request.params.data.entrySet()) {
//            if (PARAM_IGNORE.contains(entry.getKey())) {
//                continue;
//            }
//            sb.append(entry.getKey()).append(": ");
//            String[] data = entry.getValue();
//            sb.append(data.length == 1 ? data[0] : Arrays.toString(data)).append("\n");
//        }
//        return sb.toString();
//    }
}
