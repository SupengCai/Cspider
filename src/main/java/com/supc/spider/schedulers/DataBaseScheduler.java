package com.supc.spider.schedulers;

import com.supc.model.service.ContentService;
import com.supc.model.service.LinkService;
import com.supc.model.service.SiteService;
import com.supc.model.entity.Link;
import com.supc.model.entity.Site;
import com.supc.spider.entity.Content;
import com.supc.spider.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    SiteService siteService;
    @Autowired
    LinkService linkService;
    @Autowired
    ContentService contentService;

    /**
     * @param rule
     */
    @Override
    public void parseContents(Rule rule) {
        Site siteEntity = getSiteByUrl(site.getUrl().toExternalForm());
        super.parseContents(rule);
        for (Content content : contents) {
            insertLink(content, siteEntity.getId());
        }
        /** java 8 */
//        contents.forEach(item -> insertLink(item));
    }

    /**
     * @param content
     * @param siteId
     */
    private void insertLink(Content content, long siteId) {
        if (!CollectionUtils.isEmpty(content.getSubContents())) {
            Link link = new Link();
            link.setUrl(content.getUrl());
            link.setTitle(content.getTitle());
            link.setSiteId(siteId);
            linkService.save(link);

            for (Content sub : content.getSubContents()) {
                insertContent(sub, link.getId());
            }
        }
    }

    /**
     * @param content
     * @param linkId
     */
    private void insertContent(Content content, long linkId) {
        com.supc.model.entity.Content contentEntity = new com.supc.model.entity.Content();
        contentEntity.setLinkId(linkId);
        contentEntity.setContent(content.getContent());
        contentEntity.setOrdinal(content.getIndex());
        contentEntity.setType(content.getType());
        contentService.save(contentEntity);
    }

    /**
     * @param url
     * @return
     */
    public Site getSiteByUrl(String url) {
        Site site = siteService.getByUrl(url);
        if (site == null) {
            site = new Site();
            site.setUrl(this.site.getUrl().toExternalForm());
            site.setName(this.site.getName());
            site.setTopicId(Integer.valueOf(this.site.getTopic()));
            siteService.save(site);
        }
        return site;
    }

}
