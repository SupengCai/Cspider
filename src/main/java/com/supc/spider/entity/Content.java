package com.supc.spider.entity;

import com.supc.model.entity.ContentType;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/19
 */
public class Content {
    private String title;
    private String url;
    private String content;
    private int index = 0;
    private ContentType type;
    private List<Content> subContents = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public List<Content> getSubContents() {
        return subContents;
    }

    public void setSubContents(List<Content> subContents) {
        this.subContents = subContents;
    }
}
