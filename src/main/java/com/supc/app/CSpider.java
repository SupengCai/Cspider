package com.supc.app;

import com.supc.spider.engines.DefaultEngine;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/11/11
 */
public class CSpider {
    private static final Logger logger = LoggerFactory.getLogger(CSpider.class);

    public static void main(String args[]) {

        DefaultEngine defaultEngine = new DefaultEngine();
        defaultEngine.start();
        try {
            do {
                Thread.sleep(DateUtils.MILLIS_PER_HOUR);
            } while (DefaultEngine.started && DefaultEngine.initialized);
        } catch (Exception e) {
            logger.error(e.toString());
        }

    }
}
