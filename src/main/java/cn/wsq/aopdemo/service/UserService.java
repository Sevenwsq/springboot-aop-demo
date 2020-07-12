package cn.wsq.aopdemo.service;

import cn.wsq.aopdemo.dao.UserDao;
import cn.wsq.aopdemo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Silent
 * @date 2020-7-12 11:59:35
 * @description
 */
public interface UserService {

    User save(User user);

    List<User> getList();

}
