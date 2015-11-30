package com.supc.model.service;

import com.supc.model.entity.Content;
import com.supc.spider.support.hibernate.HibernateRepo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/21
 */
@Component
public class ContentService extends HibernateRepo<Content> {

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ContentService() {
        super(Content.class);
    }

    @Override
    public Content save(Content content) {
        content.setCreateAt(System.currentTimeMillis());
        return super.save(content);
    }
}
