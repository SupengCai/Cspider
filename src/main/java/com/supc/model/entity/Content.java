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
@Table(name = "cs_content")
public class Content {

    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
    private Long id;

    @Column(name = "link_id", nullable = false)
    private long linkId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "ordinal", nullable = false)
    private int ordinal;

    @Column(name = "type", nullable = false, precision = 1)
    private ContentType type;

    @Column(name = "create_at", nullable = false)
    private long createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
