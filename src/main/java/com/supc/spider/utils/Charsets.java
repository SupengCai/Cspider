package com.supc.spider.utils;

import java.nio.charset.Charset;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-10-11
 */
public final class Charsets {

    public static final String UTF8 = "UTF-8".intern();
    public static final String GBK = "GBK";
    public static final String ISO88591 = "ISO-8859-1";
    public static final Charset CHARSET_UTF8 = Charset.forName(UTF8);
    public static final Charset CHARSET_GBK = Charset.forName(GBK);
    public static final Charset CHARSET_ISO88591 = Charset.forName(ISO88591);

    private Charsets() {
    }
}
