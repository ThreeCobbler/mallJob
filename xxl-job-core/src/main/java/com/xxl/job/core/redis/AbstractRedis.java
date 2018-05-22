package com.xxl.job.core.redis;

import org.springframework.data.redis.core.*;

import javax.annotation.PostConstruct;

/**
 * @author ljn
 * @date 2018/5/22.
 */
public abstract class AbstractRedis {

    protected ValueOperations<String,String> valueOps;

    protected SetOperations<String,String> setOperations;

    protected ZSetOperations<String,String> zSetOperations;

    protected ListOperations<String,String> listOperations;

    public AbstractRedis() {
    }

    @PostConstruct
    private void afterInitialization(){
        this.valueOps = getTemplate().opsForValue();
        this.setOperations = getTemplate().opsForSet();
        this.zSetOperations = getTemplate().opsForZSet();
        this.listOperations = getTemplate().opsForList();
    }

    /**
     * @return the template
     */
    protected abstract StringRedisTemplate getTemplate();
}
