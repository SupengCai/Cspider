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
@Table(name = "cs_link")
public class Link {

    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
    private Long id;

    @Column(name = "parent_id")
    private long parentId;

    @Column(name = "site_id", nullable = false)
    private long siteId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "create_at", nullable = false)
    private long createAt;

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

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

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

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
