package com.supc.spider.spiders;


import com.supc.spider.Spider;
import com.supc.spider.entity.Rule;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/10/29
 */
public abstract class AbstractSpider implements Spider {

    final Logger logger = LoggerFactory.getLogger(AbstractSpider.class);

    public List<Element> parseBlocks(String html, Rule rule) {
        List<Element> contentList = new ArrayList<>();
        Document document = Jsoup.parse(StringUtils.defaultString(html, StringUtils.EMPTY));
        for (Element e : rule.getBlocks()) {
            Element block = document.select(e.cssSelector()).first();
            if (block != null && block.childNodeSize() > 0) {
                parseNodes(block.children(), rule.getContents(), contentList);
            }
        }
        return contentList;
    }

    private void parseNodes(Elements elements, Elements contents, List<Element> list) {
        for (Element element : elements) {
            for (Element e : contents) {
                if (element.tagName().equals(e.tagName())) {
                    if (!list.contains(element))
                        list.add(element);
                    continue;
                }
            }
            if (element.childNodeSize() > 0) {
                parseNodes(element.children(), contents, list);
            }
        }
    }
}
