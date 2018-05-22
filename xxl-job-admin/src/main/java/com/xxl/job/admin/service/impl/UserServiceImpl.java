package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.model.User;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.dao.XxlJobUserDao;
import com.xxl.job.admin.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * @author ljn
 * @date 2018/5/21.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private XxlJobUserDao  userDao;

    @Override
    public User findByNameAndPassword(String userName, String password) {
        return userDao.findByNameAndPassword(userName, password);
    }

}
