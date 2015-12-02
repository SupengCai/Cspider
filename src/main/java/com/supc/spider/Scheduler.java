package com.supc.spider;

import com.supc.spider.entity.Rule;
import com.supc.spider.entity.Site;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Properties;

/**
 * Created by caisupeng on 15/10/15.
 */
public interface Scheduler {

//    /**
//     * Get the com.supc.spider list.
//     *
//     * @return name
//     */
//    List<Spider> getSpiders();
//
//    /**
//     * Get the site's name.
//     *
//     * @return name
//     */
//    void addUrlList(String domain, List<String> urls);
//
//    /**
//     * Get the site's name.
//     *
//     * @return name
//     */
//    String nextUrl(String domain);
//
//    /**
//     * Get the site's name.
//     *
//     * @return name
//     */
//    void start();

    void init(Properties configuration);

    void start();

    void stop();


    Rule getRule();

    void parseContents(Site site, Rule rule);

    void run();

    void setDownloader(Downloader downloaders);

    void setSpider(Spider spiders);

    void setRule(Rule rule);

    void setSites(List<Site> sites);

    void addContents(List<Element> elements, String host);

    void clearContents(int maxSize, double percent);
}
