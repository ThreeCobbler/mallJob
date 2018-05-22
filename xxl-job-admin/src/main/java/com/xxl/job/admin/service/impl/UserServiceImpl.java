package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.model.User;
import com.xxl.job.admin.dao.XxlJobUserDao;
import com.xxl.job.admin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ljn
 * @date 2018/5/21.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private XxlJobUserDao  userDao;

    @Override
    public boolean findByNameAndPassword(String userName, String password) {
        User user = userDao.findByNameAndPassword(userName, password);
        if (user == null) {
            return false;
        }

        return true;
    }
}
