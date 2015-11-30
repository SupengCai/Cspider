package com.supc.spider.schedulers;


import com.supc.model.entity.ContentType;
import com.supc.spider.Downloader;
import com.supc.spider.Scheduler;
import com.supc.spider.Spider;
import com.supc.spider.entity.Content;
import com.supc.spider.entity.Rule;
import com.supc.spider.entity.Site;
import com.supc.spider.utils.HtmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.supc.spider.support.parser.ConfigurationParser;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/10/29
 */
public abstract class AbstractScheduler implements Scheduler, Runnable {

    final Logger logger = LoggerFactory.getLogger(AbstractScheduler.class);

    /**
     * Scheduled thread Pool
     */
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    /**
     * Site
     */
    protected Site site;
    /**
     * Downloaders
     */
    protected Downloader downloader;
    /**
     * Spiders
     */
    protected Spider spider;
    /**
     * Scheduler
     */
    protected ScheduledFuture scheduledFuture;
    /**
     *
     */
    protected List<Element> contentList = new ArrayList<>(DEFAULT_MAX_CACHE_SIZE / 10);
    /**
     *
     */
    protected List<Element> contentCache = new ArrayList<>(DEFAULT_MAX_CACHE_SIZE / 10);
    /**
     *
     */
    protected List<Content> contents = new ArrayList<>();
    /**
     * Is the scheduler initialized
     */
    private boolean initialized = false;
    /**
     * Is the scheduler started
     */
    private boolean started = false;
    /**
     * scheduler delay time
     */
    private static long DELAY_TIME = 0;
    /**
     *
     */
    private static int DELAY_COUNT = 1;
    /**
     *
     */
    private static int DEFAULT_MAX_CACHE_SIZE = 100000;
    /**
     *
     */
    private static double DEFAULT_CACHE_CAPACITY = 0.1;

    /**
     * init
     *
     * @param configuration
     */
    public void init(Properties configuration) {
        if (!initialized) {
            DELAY_TIME = configuration.containsKey(ConfigurationParser.CSPIDER_SCHEDULER_DELAY) ? Integer.valueOf(configuration.getProperty(ConfigurationParser.CSPIDER_SCHEDULER_DELAY)) : 10;
            try {
                downloader = (Downloader) Class.forName(site.getDownloader()).newInstance();
                spider = (Spider) Class.forName(site.getSpider()).newInstance();
                logger.info("downloader name :" + downloader.toString());
                logger.info("spider name :" + spider.toString());
            } catch (Exception e) {
                logger.error(e.toString());
            }
            initialized = true;
        }
    }

    public void start() {
        if (!started) {
            if (!initialized) throw new RuntimeException("call init() before start the scheduler");
            synchronized (Scheduler.class) {
                logger.info("scheduler start :");
                scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(this, 2 * DELAY_COUNT++, DELAY_TIME, TimeUnit.SECONDS);
            }
            started = true;
        }
    }

    public void stop() {
        if (started) {
            scheduledFuture.cancel(true);
            started = false;
        }
    }

    public void run() {
        logger.info("scheduler running ... :");
        Rule rule = getRule();
        List<Element> list = spider.parseBlocks(downloader.httpGet(site.getUrl().toExternalForm()), rule);
        addContents(list);
        parseContents(rule.getSubRule());
        clearContents(DEFAULT_MAX_CACHE_SIZE, DEFAULT_CACHE_CAPACITY);
        logger.info("scheduler done.");
    }

    /**
     * @param list
     */
    private void addContents(List<Element> list) {
        for (Element e : list) {
            if (!contentCache.contains(e)) {
                contentList.add(e);
                Content content = new Content();
                content.setTitle(e.text());
                content.setUrl(e.attr(HtmlUtils.TAG_ATTRIBUTE_HREF));
                contents.add(content);
            }
        }
    }

    /**
     * @param rule
     */
    public void parseContents(Rule rule) {

        for (Content content : contents) {
            List<Element> list = spider.parseBlocks(downloader.httpGet(HtmlUtils.validateUrl(content.getUrl(), site.getUrl().getHost())), rule);
            if (!CollectionUtils.isEmpty(list)) {
                parseContent(list, content.getSubContents(), rule.getContents());
            }
        }
    }

    /**
     * @param elements
     * @param subContents
     * @param contents
     */
    private void parseContent(List<Element> elements, List<Content> subContents, Elements contents) {
        int index = 0;
        for (Element element : elements) {
            for (Element c : contents) {
                if (element.tagName().equals(c.tagName())) {
                    Content content = getContent(element, c.attributes(), index);
                    if (StringUtils.isNotEmpty(content.getContent())) {
                        subContents.add(content);
                        index++;
                    }
                }
            }
        }
    }

    /**
     * @param element
     * @param attributes
     */
    private Content getContent(Element element, Attributes attributes, int index) {
        Content content = new Content();
        if (attributes.size() > 0) {
            for (Attribute attr : attributes) {
                String key = attr.getKey();
                if (element.hasAttr(key)) {
                    content.setContent(element.attr(key));
                    break;
                }
            }
        } else {
            content.setContent(element.html());
        }
        if (StringUtils.isNotEmpty(content.getContent())) {
            content.setContent(HtmlUtils.htmlTagFilter(content.getContent()));
            content.setIndex(index);
            content.setType(getContentType(element.tagName()));
        }
        return content;
    }

    /**
     * @param tagName
     * @return
     */
    private ContentType getContentType(String tagName) {
        ContentType type;
        switch (tagName) {
            case HtmlUtils.HTML_TAG_A:
                type = ContentType.URL;
                break;
            case HtmlUtils.HTML_TAG_IMG:
                type = ContentType.IMAGE;
                break;
            case HtmlUtils.HTML_TAG_EMBED:
                type = ContentType.VIDEO;
                break;
            case HtmlUtils.HTML_TAG_P:
            default:
                type = ContentType.TEXT;
                break;
        }
        return type;
    }

    /**
     * @param maxSize
     * @param percent
     */
    protected void clearContents(int maxSize, double percent) {
        contents.clear();
        contentCache.addAll(contentList);
        contentList.clear();
        while (contentCache.size() > maxSize)
            clearList(contentCache, percent);
    }

    /**
     * Removes part of the listings from this list.
     * The list will be reduce after this call returns.
     *
     * @param list
     * @param percent
     */
    private void clearList(List<Element> list, double percent) {
        int count = 0;
        int num = (int) (list.size() * percent);
        while (count++ < num) {
            list.remove(0);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractScheduler) {
            AbstractScheduler other = (AbstractScheduler) obj;
            return site.getName().equals(other.site.getName()) && site.getRule().equals(other.site.getRule());
        }
        return false;
    }

    public Rule getRule() {
        return this.site.getRule();
    }

    public String getName() {
        return StringUtils.defaultString(site.getName(), StringUtils.EMPTY);
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public void setDownloader(Downloader downloaders) {
        this.downloader = downloaders;
    }

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spiders) {
        this.spider = spiders;
    }

}
