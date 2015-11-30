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
    private Rule rule;
    private String topic;
    private String scheduler;
    private String spider;
    private String downloader;

    public Site(String name, URL url, String topic, Rule rule, String scheduler, String spider, String downloader) {
        this.name = name;
        this.url = url;
        this.topic = topic;
        this.rule = rule;
        this.scheduler = scheduler;
        this.spider = spider;
        this.downloader = downloader;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Site) {
            Site other = (Site) obj;
            if ((this.name.equals(other.name)) && (this.url.equals(other.url)
                    && (this.rule.equals(other.rule))))
                return true;
        }
        return false;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
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

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public String getSpider() {
        return spider;
    }

    public void setSpider(String spider) {
        this.spider = spider;
    }

    public String getDownloader() {
        return downloader;
    }

    public void setDownloader(String downloader) {
        this.downloader = downloader;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
