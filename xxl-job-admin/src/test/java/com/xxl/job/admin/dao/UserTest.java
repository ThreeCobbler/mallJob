package com.xxl.job.admin.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xxl.job.admin.core.model.User;
import com.xxl.job.admin.service.IUserRedis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @author ljn
 * @date 2018/5/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationcontext-*.xml")
public class UserTest {

    @Autowired
    private XxlJobUserDao userDao;

    @Autowired
    IUserRedis userRedis;


    @Test
    public void test1(){
        User user = userDao.findByNameAndPassword("a", "a");
        System.out.println(user);
    }

    @Test
    public void test2() throws JsonProcessingException {
        User user = new User();
        user.setUserName("aaa");
        user.setPassword("aaa");
        userRedis.addToken("token",user);
    }

    @Test
    public void test3() throws IOException {
        User user = userRedis.getUser("token");
        System.out.println(user);
    }

    @Test
    public void test4(){
        userRedis.delete("token");
    }

}
