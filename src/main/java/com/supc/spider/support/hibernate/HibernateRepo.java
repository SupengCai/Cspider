package com.supc.spider.support.hibernate;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/23
 */
public class HibernateRepo<T> implements Repo<T> {
    protected Logger logger;

    private final Class clazz;

    protected SessionFactory sessionFactory;

    public HibernateRepo(Class clazz) {
        this.clazz = clazz;
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public T save(T t) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(t);
            transaction.commit();
            session.close();
            return t;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    public T get(long id) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            T t = (T) session.get(clazz, id);
            transaction.commit();
            session.close();
            return t;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    public List find(String sql) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql).addEntity(clazz);
            List ts = query.list();
            transaction.commit();
            session.close();
            return ts;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
