package com.supc.spider.support.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V1.0, 15/12/1
 */
@Component
public class RedisService extends RedisReop {

    @Autowired
    @Override
    public void setTemplate(RedisTemplate template) {
        this.template = template;
    }

    final Logger logger = LoggerFactory.getLogger(RedisService.class);
}
