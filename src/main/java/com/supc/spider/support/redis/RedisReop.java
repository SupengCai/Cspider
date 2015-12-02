package com.supc.spider.support.redis;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.JacksonHashMapper;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:spcai@wisorg.com">spcai</a>
 * @version V.0
 */
public class RedisReop {

    private static Jedis jedis;

    protected RedisTemplate template;

    private String redisPrefix;

    /**
     * @return the template
     */
    public RedisTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(RedisTemplate template) {
        this.template = template;
    }


    public void hset(String hashKey, String hashValue) {
        template.boundHashOps(redisPrefix).put(hashKey, hashValue);
    }

    public String hget(String hashKey) {
        return (String) template.boundHashOps(redisPrefix).get(hashKey);
    }

    public boolean hasKey(String hashKey) {
        return template.boundHashOps(redisPrefix).hasKey(hashKey);
    }

    public void set(String hashKey, String hashValue) {
        template.boundValueOps(redisPrefix + hashKey).set(hashValue);
    }

    public String get(String hashKey) {
        return (String) template.boundValueOps(redisPrefix + hashKey).get();
    }

    public void setEx(String hashKey, String hashValue, long timeout, TimeUnit unit) {
        BoundValueOperations op = template.boundValueOps(redisPrefix + hashKey);
        op.set(hashValue);
        op.expire(timeout, unit);
    }

    public String getRedisPrefix() {
        return redisPrefix;
    }

    public void setRedisPrefix(String redisPrefix) {
        this.redisPrefix = redisPrefix;
    }
}
