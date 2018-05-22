package com.xxl.job.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xxl.job.admin.core.model.User;

import java.io.IOException;

/**
 * @author ljn
 * @date 2018/5/22.
 */
public interface IUserRedis {

    /**
     * 保存token
     * @param token
     * @param user
     */
    public void addToken(String token,User user) throws JsonProcessingException;

    User getUser(String token) throws IOException;

    void delete(String token);


}
