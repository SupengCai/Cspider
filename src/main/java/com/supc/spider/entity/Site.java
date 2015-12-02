package com.supc.spider.entity;

import java.net.URL;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/13
 */
public class Site {
    private String name = "";
    private URL url;
    private String topic;

    public Site(String name, URL url, String topic) {
        this.name = name;
        this.url = url;
        this.topic = topic;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Site) {
            Site other = (Site) obj;
            if ((this.name.equals(other.name)) && (this.url.equals(other.url)))
                return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
