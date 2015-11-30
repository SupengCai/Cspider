package com.supc.model.service;

import com.supc.model.entity.Link;
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
public class LinkService extends HibernateRepo<Link> {

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public LinkService() {
        super(Link.class);
    }

    @Override
    public Link save(Link link) {
        link.setCreateAt(System.currentTimeMillis());
        return super.save(link);
    }
}
