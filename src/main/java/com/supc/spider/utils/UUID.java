/*
 * Project:  msc-parent
 * Module:   msc-server
 * File:     UUID.java
 * Modifier: xyang
 * Modified: 2014-08-16 11:10
 *
 * Copyright (c) 2014 Wisorg All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.supc.spider.utils;


/**
 * .
 * <p/>
 *
 * @author <a href="mailto:xyang@wisorg.com">xyang</a>
 * @version V1.0, 14-8-16
 */
public final class UUID {
    private static final Snowflake idWorker;

    static {
        idWorker = Snowflake.getInstance(1);
    }

    public static long next() {
        return idWorker.nextId();
    }

    public static String nextHex() {
        return Long.toHexString(next());
    }

    public static String nextStr() {
        return Codecs.encode(next());
    }

    private UUID() {
    }
}
