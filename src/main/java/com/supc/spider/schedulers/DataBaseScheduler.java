package com.supc.spider.schedulers;

import com.supc.model.entity.ContentEntity;
import com.supc.model.entity.ContentType;
import com.supc.model.entity.Link;
import com.supc.model.entity.SiteEntity;
import com.supc.model.service.ContentService;
import com.supc.model.service.LinkService;
import com.supc.model.service.SiteService;
import com.supc.spider.entity.Content;
import com.supc.spider.entity.Rule;
import com.supc.spider.entity.Site;
import com.supc.spider.support.parser.ConfigurationParser;
import com.supc.spider.support.redis.RedisService;
import com.supc.spider.utils.HtmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/19
 */
@Scope("prototype")
@Component
public class DataBaseScheduler extends AbstractScheduler {

    final Logger logger = LoggerFactory.getLogger(DataBaseScheduler.class);

    @Autowired
    SiteService siteService;
    @Autowired
    LinkService linkService;
    @Autowired
    ContentService contentService;
    @Autowired
    RedisService redisService;

    private long timeout;

    @Override
    public void init(Properties configuration) {
        super.init(configuration);
        redisService.setRedisPrefix("cspider:links:");
        long time = Long.valueOf(configuration.getProperty(ConfigurationParser.CSPIDER_CACHE_EXPIRE));
        timeout = (time > 0L && time < 60L) ? timeout : 60L;
    }

    /**
     * Add Content objects to list which has not being parsed
     *
     * @param links
     * @param host
     */
    @Override
    public void addContents(List<Element> links, String host) {
        for (Element link : links) {
            if (StringUtils.isNotEmpty(link.text()) && redisService.get(link.outerHtml()) == null) {
                redisService.setEx(link.outerHtml(), StringUtils.EMPTY, timeout, TimeUnit.DAYS);
                Content content = new Content();
                content.setTitle(link.text());
                content.setUrl(HtmlUtils.validateUrl(link.attr(HtmlUtils.TAG_ATTRIBUTE_HREF), host));
                contents.add(content);
            }
        }
    }

    /**
     * Parse html to Content object
     *
     * @param site
     * @param rule
     */
    @Override
    public void parseContents(com.supc.spider.entity.Site site, Rule rule) {
        SiteEntity siteEntity = getSiteByUrl(site);
        super.parseContents(site, rule);
        for (Content content : contents) {
            insertLink(content, siteEntity.getId(), site.getUrl().getHost());
        }
    }

    /**
     * Create LinkEntity object by html and insert into mysql
     *
     * @param content
     * @param siteId
     */
    private void insertLink(Content content, long siteId, String host) {
        if (!CollectionUtils.isEmpty(content.getSubContents())) {
            Link link = new Link();
            link.setUrl(HtmlUtils.validateUrl(content.getUrl(), host));
            link.setTitle(content.getTitle());
            link.setSiteId(siteId);
            linkService.save(link);
            for (Content sub : content.getSubContents()) {
                insertContent(sub, link.getId(), host);
            }
        }
    }

    /**
     * Create ContentEntity object by html and insert into mysql
     *
     * @param content
     * @param linkId
     */
    private void insertContent(Content content, long linkId, String host) {
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setLinkId(linkId);
        contentEntity.setOrdinal(content.getIndex());
        contentEntity.setType(content.getType());
        String str = content.getContent();
        if (content.getType() != ContentType.TEXT) {
            contentEntity.setContent(HtmlUtils.validateUrl(str, host));
        } else {
            contentEntity.setContent(str);
        }
        contentService.save(contentEntity);
    }

    /**
     * Query url in mysql, if not exist insert and return
     *
     * @param site
     * @return
     */
    public SiteEntity getSiteByUrl(Site site) {
        SiteEntity siteEntity = siteService.getByUrl(site.getUrl().toExternalForm());
        if (siteEntity == null) {
            siteEntity = new SiteEntity();
            siteEntity.setUrl(site.getUrl().toExternalForm());
            siteEntity.setName(site.getName());
            siteEntity.setTopicId(Integer.valueOf(site.getTopic()));
            siteService.save(siteEntity);
        }
        return siteEntity;
    }
}
