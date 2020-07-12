package cn.wsq.aopdemo.dao;

import cn.wsq.aopdemo.entity.User;

import java.util.List;

/**
 * @author Silent
 * @date 2020-7-12 11:48:57
 * @description
 */
public interface UserDao {

    User save(User user);

    List<User> getList();

}
