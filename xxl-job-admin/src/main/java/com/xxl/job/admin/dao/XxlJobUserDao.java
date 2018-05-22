package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author ljn
 * @date 2018/5/21.
 */
public interface XxlJobUserDao {

    /**
     * 根据有户名和密码查询用户
     * @param userName
     * @param password
     * @return
     */
    User findByNameAndPassword(@Param("userName")String userName, @Param("password")String password);
}
