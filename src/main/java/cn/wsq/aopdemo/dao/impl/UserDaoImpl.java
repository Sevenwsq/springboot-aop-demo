package cn.wsq.aopdemo.dao.impl;

import cn.wsq.aopdemo.dao.UserDao;
import cn.wsq.aopdemo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Silent
 * @date 2020-7-12 11:52:46
 * @description
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Override
    public User save(User user) {
        return user;
    }

    @Override
    public List<User> getList() {
        List<User> list = new ArrayList<User>(3) {{
            add(new User("jack","jack@163.com"));
            add(new User("lucy","lucy@163.com"));
            add(new User("lily","lily@163.com"));
        }};
        return list;
    }
}
