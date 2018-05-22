package com.xxl.job.admin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.admin.core.model.User;
import com.xxl.job.admin.service.IUserRedis;
import com.xxl.job.core.redis.AbstractRedis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author ljn
 * @date 2018/5/22.
 */
@Component
public class UserRedisImpl extends AbstractRedis implements IUserRedis {

    @Value(value="${LOGIN_IDENTITY_KEY}")
    private String prefix;

    @Autowired
    @Qualifier("userRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    protected StringRedisTemplate getTemplate() {
        return redisTemplate;
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void addToken(String token,User user) throws JsonProcessingException {
        valueOps.set(prefix+token,mapper.writeValueAsString(user),18000, TimeUnit.SECONDS);
    }

    @Override
    public User getUser(String token) throws IOException {
        String json = valueOps.get(prefix + token);
        if (StringUtils.isNotBlank(json)) {
            User user = mapper.readValue(json, User.class);
            return user;
        }
        return null;
    }

    @Override
    public void delete(String token) {
        redisTemplate.delete(prefix + token);
    }
}
