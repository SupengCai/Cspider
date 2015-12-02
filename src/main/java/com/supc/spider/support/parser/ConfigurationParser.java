package com.supc.spider.support.parser;

import com.supc.spider.Downloader;
import com.supc.spider.Scheduler;
import com.supc.spider.Spider;
import com.supc.spider.entity.Rule;
import com.supc.spider.entity.Site;
import com.supc.spider.utils.HtmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/23
 */
public class ConfigurationParser {

    static final Logger logger = LoggerFactory.getLogger(ConfigurationParser.class);

    /**
     * XML element
     */
    public static final String NODE_SITES = "sites";
    public static final String NODE_SITE = "site";
    public static final String NODE_RULE = "rule";
    public static final String NODE_BLOCKS = "blocks";
    public static final String NODE_CONTENTS = "contents";
    public static final String NODE_TAGS = "tags";
    public static final String NODE_TAG = "tag";
    public static final String NODE_ATTRIBUTES = "attributes";
    public static final String NODE_ATTRIBUTE = "attribute";
    public static final String NODE_SCHEDULER = "scheduler";
    public static final String NODE_SPIDER = "spider";
    public static final String NODE_DOWNLOADER = "downloader";

    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_TOPIC = "topic";
    public static final String ATTRIBUTE_URL = "url";
    public static final String ATTRIBUTE_KEY = "key";
    public static final String ATTRIBUTE_VALUE = "value";
    public static final String ATTRIBUTE_CLASS = "class";

    /**
     * properties config
     */
    public static final String CSPIDER_ENGINE = "cspider.engine";
    public static final String CSPIDER_SCHEDULER_DELAY = "cspider.scheduler.delay";
    public static final String CSPIDER_CACHE_EXPIRE = "cspider.cache.expire";

    /**
     * Fill Engine's sites list with XML config file
     *
     * @param file
     * @param schedulers
     * @throws DocumentException
     */
    public static void fillSchedulerByXML(File file, List<Scheduler> schedulers, GenericApplicationContext applicationContext) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        org.dom4j.Document doc = saxReader.read(file);
        Element root = doc.getRootElement();
        List<Element> nodes = root.elements(NODE_SCHEDULER);
        if (!CollectionUtils.isEmpty(nodes)) {
            for (Element node : nodes) {
                Scheduler scheduler = createScheduler(node, applicationContext);
                if (!schedulers.contains(scheduler))
                    schedulers.add(scheduler);
            }
        }
    }

    /**
     * Create Scheduler object by <scheduler> element
     *
     * @param node
     * @return
     */
    public static Scheduler createScheduler(Element node, GenericApplicationContext applicationContext) {
        Scheduler scheduler = (Scheduler) applicationContext.getBean(node.attributeValue(ATTRIBUTE_CLASS));
        try {
            String downloaderClazz = node.element(NODE_DOWNLOADER).attributeValue(ATTRIBUTE_CLASS);
            String spiderClazz = node.element(NODE_SPIDER).attributeValue(ATTRIBUTE_CLASS);

            Downloader downloader = (Downloader) Class.forName(downloaderClazz).newInstance();
            Spider spider = (Spider) Class.forName(spiderClazz).newInstance();
            Rule rule = createRule(new Rule(), node.element(NODE_RULE));

            List<Site> sites = new ArrayList<>();
            List<Element> sitesNode = node.element(NODE_SITES).elements(NODE_SITE);
            for (Element siteNode : sitesNode) {
                Site site = createSite(siteNode);
                if (!sites.contains(site)) {
                    sites.add(site);
                }
            }
            scheduler.setRule(rule);
            scheduler.setDownloader(downloader);
            scheduler.setSpider(spider);
            scheduler.setSites(sites);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return scheduler;
    }

    /**
     * Create site object by <site> element
     *
     * @param node
     * @return
     */
    private static Site createSite(Element node) {
        Site site = null;
        try {
            URL url = new URL(HtmlUtils.validateUrl(node.attributeValue(ATTRIBUTE_URL), StringUtils.EMPTY));
            site = new Site(node.attributeValue(ATTRIBUTE_NAME), url, node.attributeValue(ATTRIBUTE_TOPIC));
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return site;
    }

    /**
     * Recursive call to create Rule and subRule Object
     *
     * @param rule
     * @param ruleNode
     * @return
     */
    public static Rule createRule(Rule rule, Element ruleNode) {

        List<Element> blockTags = ruleNode.element(NODE_BLOCKS).elements(NODE_TAG);
        List<Element> contentTags = ruleNode.element(NODE_CONTENTS).elements(NODE_TAG);
        Element subRule = ruleNode.element(NODE_RULE);
        convertElements(blockTags, rule.getBlocks());
        convertElements(contentTags, rule.getContents());
        if (subRule != null) {
            rule.setSubRule(createRule(new Rule(), subRule));
        }
        return rule;
    }

    /**
     * Convert element from dom4j to jsoup
     *
     * @param tags
     * @param elements
     */
    public static void convertElements(List<Element> tags, Elements elements) {
        for (Element tag : tags) {
            org.jsoup.nodes.Element node = new org.jsoup.nodes.Element(Tag.valueOf(tag.attributeValue(ATTRIBUTE_NAME)), "");
            Element attributes = tag.element(NODE_ATTRIBUTES);
            if (attributes != null) {
                List<Element> nodeAttrs = attributes.elements();
                for (Element attr : nodeAttrs) {
                    String value = attr.attributeValue(ATTRIBUTE_VALUE);
                    if (StringUtils.isNotEmpty(value)) {
                        node.attr(attr.attributeValue(ATTRIBUTE_KEY), value);
                    } else {
                        node.attr(attr.attributeValue(ATTRIBUTE_KEY), StringUtils.EMPTY);
                    }
                }
            }
            elements.add(node);
        }
    }
}
