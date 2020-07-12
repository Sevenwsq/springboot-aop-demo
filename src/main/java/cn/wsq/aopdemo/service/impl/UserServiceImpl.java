package cn.wsq.aopdemo.service.impl;

import cn.wsq.aopdemo.dao.UserDao;
import cn.wsq.aopdemo.entity.User;
import cn.wsq.aopdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Silent
 * @date 2020-7-12 12:00:34
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) {
        System.out.println("UserServiceImpl.save 添加用户：" + user);
//        int a = 10/0;
        return userDao.save(user);
    }

    @Override
    public List<User> getList() {
        List<User> list = userDao.getList();
        System.out.println("UserServiceImpl.getList 查询所有用户：" + list);
        return list;
    }

}
