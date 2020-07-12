package cn.wsq.aopdemo.service.impl;

import cn.wsq.aopdemo.annotation.LogAnnotation;
import cn.wsq.aopdemo.dao.UserDao;
import cn.wsq.aopdemo.entity.User;
import cn.wsq.aopdemo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Silent
 * @date 2020-7-12 12:00:34
 * @description 用于测试自定义注解实现aop
 */
@Service
public class UserServiceImpl2 implements UserService {

    private final UserDao userDao;

    public UserServiceImpl2(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @LogAnnotation(value="添加用户")
    public User save(User user) {
        System.out.println("UserServiceImpl.save 添加用户：" + user);
        return userDao.save(user);
    }

    @Override
    public List<User> getList() {
        List<User> list = userDao.getList();
        System.out.println("UserServiceImpl.getList 查询所有用户：" + list);
        return list;
    }

}
