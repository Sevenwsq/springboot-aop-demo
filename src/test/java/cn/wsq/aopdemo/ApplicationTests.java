package cn.wsq.aopdemo;

import cn.wsq.aopdemo.entity.User;
import cn.wsq.aopdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserService userServiceImpl;

    @Test
    void testAspect() {
//        List<User> list = userServiceImpl.getList();
//        System.out.println(list);
        User user = userServiceImpl.save(new User("test","test@163.com"));
        System.out.println("切面执行完毕，返回值："+user);
    }

    @Autowired
    private UserService userServiceImpl2;

    @Test
    void testAspectAnnotation() {
//        List<User> list = userServiceImpl2.getList();
//        System.out.println(list);
        User user = userServiceImpl2.save(new User("test","test@163.com"));
        System.out.println("切面执行完毕，返回值："+user);
    }

}