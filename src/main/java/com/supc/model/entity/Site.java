package com.supc.model.entity;

import com.supc.spider.support.hibernate.SnowflakeGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/12
 */

@Entity
@Table(name = "cs_site", uniqueConstraints = {@UniqueConstraint(columnNames = {"url"})})
public class Site {

    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
    private Long id;

    @Column(name = "parent_id")
    private long parentId;

    @Column(name = "topic_id", nullable = false)
    private long topicId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
