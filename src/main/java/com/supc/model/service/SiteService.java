package com.supc.model.service;

import com.supc.model.entity.SiteEntity;
import com.supc.spider.support.hibernate.HibernateRepo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/21
 */
@Component
public class SiteService extends HibernateRepo<SiteEntity> {

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SiteService() {
        super(SiteEntity.class);
    }

    public SiteEntity getByUrl(String url) {
        List<SiteEntity> list = find("select * from cs_site where url='" + url + "'");
        return list.isEmpty() ? null : list.get(0);
    }
}
