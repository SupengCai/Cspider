package com.supc.spider.entity;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/17
 */
public class Rule {
    private Rule subRule;
    private Elements blocks;
    private Elements contents;
    private List<Element> contentList = new ArrayList<>();

    public Rule() {
        blocks = new Elements();
        contents = new Elements();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule) {
            Rule other = (Rule) obj;
            if (((this.subRule == null && other.subRule == null) || (this.subRule.equals(other.subRule)))
                    && (this.blocks.equals(other.blocks)
                    && (this.contents.equals(other.contents))))
                return true;
        }
        return false;
    }

    public Elements getBlocks() {
        return blocks;
    }

    public void setBlocks(Elements blocks) {
        this.blocks = blocks;
    }

    public Elements getContents() {
        return contents;
    }

    public void setContents(Elements contents) {
        this.contents = contents;
    }

    public Rule getSubRule() {
        return subRule;
    }

    public void setSubRule(Rule subRule) {
        this.subRule = subRule;
    }

    public List<Element> getContentList() {
        return contentList;
    }

    public void setContentList(List<Element> contentList) {
        this.contentList = contentList;
    }
}
