package com.xxl.job.admin.service;

import com.xxl.job.admin.core.model.User;

/**
 * @author ljn
 * @date 2018/5/21.
 */
public interface IUserService {

    /**
     * 根据有户名和密码查询用户
     * @param userName
     * @param password
     * @return
     */
    User findByNameAndPassword(String userName, String password);
}
