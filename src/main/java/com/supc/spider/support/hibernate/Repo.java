package com.supc.spider.support.hibernate;

import org.hibernate.SQLQuery;

import java.util.List;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/23
 */
interface Repo<T> {
    T save(T t);

    T get(long id);

    List find(String sql);
}
