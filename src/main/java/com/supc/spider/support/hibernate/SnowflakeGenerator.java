/*
 * Project:  msc-parent
 * Module:   msc-server
 * File:     LongGenerator.java
 * Modifier: xyang
 * Modified: 2014-08-16 11:09
 *
 * Copyright (c) 2014 Wisorg All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.supc.spider.support.hibernate;

import com.supc.spider.utils.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:xyang@wisorg.com">xyang</a>
 * @version V1.0, 14-8-16
 */
public class SnowflakeGenerator implements IdentifierGenerator {
    public static final String TYPE = "com.supc.spider.support.hibernate.SnowflakeGenerator";

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return UUID.next();
    }
}
