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
     * SiteEntity
     */
    private List<Site> sites;
    /**
     * Downloader
     */
    private Downloader downloader;
    /**
     * Spider
     */
    private Spider spider;
    /**
     * Rule
     */
    private Rule rule;
    /**
     * ScheduledFuture
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
        for (Site site : sites) {
            logger.info("site " + site.getName() + " is running ... :");
            List<Element> elements = spider.parseBlocks(downloader.httpGet(site.getUrl().toExternalForm()), rule);
            addContents(elements, site.getUrl().getHost());
            parseContents(site, rule.getSubRule());
            clearContents(DEFAULT_MAX_CACHE_SIZE, DEFAULT_CACHE_CAPACITY);
            logger.info("site " + site.getName() + " is done :");
        }
        logger.info("scheduler done.");
    }

    /**
     * Add Content objects to list which has not being parsed
     *
     * @param links
     * @param host
     */
    public void addContents(List<Element> links, String host) {
        for (Element link : links) {
            if (StringUtils.isNotEmpty(link.text()) && !contentCache.contains(link)) {
                contentList.add(link);
                Content content = new Content();
                content.setTitle(link.text());
                content.setUrl(HtmlUtils.validateUrl(link.attr(HtmlUtils.TAG_ATTRIBUTE_HREF), host));
                contents.add(content);
            }
        }
    }

    /**
     * parse Contents of the Site with specific configuration rule
     *
     * @param site
     * @param rule
     */
    public void parseContents(Site site, Rule rule) {

        for (Content content : contents) {
            List<Element> links = spider.parseBlocks(downloader.httpGet(HtmlUtils.validateUrl(content.getUrl(), site.getUrl().getHost())), rule);
            if (!CollectionUtils.isEmpty(links)) {
                parseContent(links, content.getSubContents(), rule.getContents());
            }
        }
    }

    /**
     * Parse html to Content object
     *
     * @param links
     * @param subContents
     * @param ruleContents
     */
    private void parseContent(List<Element> links, List<Content> subContents, Elements ruleContents) {
        int index = 0;
        for (Element link : links) {
            for (Element rule : ruleContents) {
                if (link.tagName().equals(rule.tagName())) {
                    Content content = getContent(link, rule.attributes(), index);
                    if (StringUtils.isNotEmpty(content.getContent())) {
                        subContents.add(content);
                        index++;
                    }
                }
            }
        }
    }

    /**
     * Fill the Content attributes according to the configuration rule
     *
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
     * Get type via tag name
     *
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
     * clear parsed contents chche
     *
     * @param maxSize
     * @param percent
     */
    public void clearContents(int maxSize, double percent) {
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
            return rule.equals(other.rule) && sites.equals(other.sites);
        }
        return false;
    }

    public Rule getRule() {
        return this.rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
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
