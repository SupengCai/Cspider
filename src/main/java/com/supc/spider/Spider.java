package com.supc.spider;

import com.supc.spider.entity.Rule;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;

/**
 * Resource. (API, Prototype, Immutable, ThreadSafe)
 * <p>
 * <pre>
 * </pre>
 *
 * @author spcai
 */
public interface Spider {

    List<Element> parseBlocks(String html, Rule rule);

//    Map<Element, Boolean> getContentMap();
//
//    /**
//     * Get the site's name.
//     *
//     * @return name
//     */
//    String getName();
//
//    /**
//     * Get the site's url list.
//     *
//     * @return name
//     */
//    List<String> getUrls();
//
//    /**
//     * Parse html.
//     *
//     * @return name
//     */
//    void parseBlocks(String html);
}
