package com.supc.spider;

import com.supc.spider.entity.Rule;
import com.supc.spider.entity.Site;

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

    String getName();

    void setSite(Site site);

    Rule getRule();

    void parseContents(Rule rule);

    void run();

}
