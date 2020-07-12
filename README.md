# springboot-aop-demo
> AOP：Aspect Oriented Programming 面向切面编程，是一种不需要更改原先的业务代码，就能够为业务代码添加一些与其本身业务无关的附加操作的代码的技术，如日志，权限校验等等
>
> spring中使用动态代理的方式来实现aop，在代码执行的过程中动态的添加一些不影响业务代码的附加操作。

`在spring中，Aspect(切面)由poincut(切入点)和advice(通知方式)共同组成`
<img src="https://img2020.cnblogs.com/blog/1814997/202007/1814997-20200712231218497-949005518.png" alt="image-20200712185603345" style="zoom:70%;" />

**如果有用，点个Star** 😊

### 1.创建springboot项目，引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### 2.建立dao层和service层

```java
public interface UserDao {
    User save(User user);
    List<User> getList();
}
```

```java
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
```

```java
public interface UserService {
    User save(User user);
    List<User> getList();
}
```

```java
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) {
        System.out.println("添加用户：" + user);
        return userDao.save(user);
    }

    @Override
    public List<User> getList() {
        List<User> list = userDao.getList();
        System.out.println("查询所有用户：" + list);
        return list;
    }

}
```

### 3.创建切面类，并且声明切入点

`@Aspect 注解 声明这是一个切面类`

`@Component 注解 把类加入到IOC容器中  `

`@Pointcut 注解 用于指定哪些类的哪些方法需要增强。常用参数有within、execution和@annotation`

`within`表达式 的粒度比较粗，只作用到类级别，用于指定哪些类需要被增强，表达式指定的类中的所有方法都会被增强。

`execution`表达式 粒度相对较细，精确到方法级别，只要表达式指定的方法会被增强

`@annotation` 的参数是一个`注解类型`，用于匹配含有该注解的方法，进而增强

```java
@Aspect
@Component
public class LogAspect {
    /**
     * 声明切入点，可以采用within表达式，execution表达式和annotation注解式声明
     * within(cn.wsq.aopdemo.service.impl.*ServiceImpl)
     * execution(* cn.wsq.aopdemo.service.impl.*ServiceImpl.*(..))
     * @annotation(cn.wsq.aopdemo.annotation.LogAnnotation)
     */
    @Pointcut("within(cn.wsq.aopdemo.service.impl.*ServiceImpl)")
    public void pointcut() {}
}
```

### 4.编写增强执行的方法

spring中一共有五种通知方式，分别是`Around、Before、After、AfterReturning、AfterThrowing`

1. around 环绕通知

   `环绕通知是最重要的一个通知，可以用环绕通知实现其他四个通知的所有功能`

   在`point.proceed();`这行代码执行之前的代码是前置通知，之后的是后置通知，出现异常的是异常通知

   ```java
   @Aspect
   @Component
   public class LogAspect {
     	@Pointcut("within(cn.wsq.aopdemo.service.impl.*ServiceImpl)")
       public void pointcut() {}
       /**
        * 环绕通知
        * @param point 包含执行的目标方法的信息
        * @return 目标方法的返回值
        */
       @Around(value = "pointcut()")
       public Object logAround(ProceedingJoinPoint point) throws Throwable {
            Object result = null;
           try {
               System.out.println("=============环绕通知开始=============");
               MethodSignature signature = (MethodSignature) point.getSignature();
               Method method = signature.getMethod();
               System.out.println("目标方法："+ method);
               Object[] args = point.getArgs();
               System.out.println("目标方法的参数："+Arrays.toString(args));
               /*
                * point.proceed()执行目标方法,返回值是目标方法的返回值  args是执行目标方法的参数
                * 这里值的注意的是，如果不给proceed方法传参，直接point.proceed()调用，
                * 那么默认是使用目标方法的原始参数
                * 如果传参了，则使用传入的参数，在此可以对目标方法的参数进行统一包装
                * 如：point.proceed(new Object[]{new User("zs","zs@163.com")})
                * 那么参数就被修改成了User("zs","zs@163.com")，我们添加的user信息也就改变了
                * 其目的是可以对请求参数进行包装，比如解密
                **/
               result = point.proceed();
               System.out.println("执行结果："+result);
               System.out.println("=============环绕通知正常结束=============");
           } catch (Throwable throwable) {
               throwable.printStackTrace();
               System.out.println("=============环绕通知异常结束=============");
           }
           //返回目标方法的返回值,我们在返回之前可以做一些特定的操作，比如加密
           return result;
       }
   }
   ```

2. before 前置通知

   `前置通知的参数(JoinPoint point)可有可无，如果不需要获得目标方法的信息，就不需要这个参数`

   ```java
   @Aspect
   @Component
   public class LogAspect {
     	@Pointcut("within(cn.wsq.aopdemo.service.impl.*ServiceImpl)")
       public void pointcut() {}
     	/**
        * 前置通知
        * @param point 包含执行的目标方法的信息
        */
       @Before(value = "pointcut()")
       public void logBefore(JoinPoint point){
           System.out.println("=============前置通知开始=============");
           MethodSignature signature = (MethodSignature) point.getSignature();
           Method method = signature.getMethod();
           System.out.println("目标方法："+ method);
           Object[] args = point.getArgs();
           System.out.println("目标方法的参数："+Arrays.toString(args));
           System.out.println("=============前置通知结束=============");
       }
   }
   ```

3. after 后置通知

   `这里值的说明的是，无论是否发生异常，后置通知都会执行`

   ```java
   @Aspect
   @Component
   public class LogAspect {
     	@Pointcut("within(cn.wsq.aopdemo.service.impl.*ServiceImpl)")
       public void pointcut() {}
     	/**
        * 后置通知
        */
       @After(value = "pointcut()")
       public void logAfter(){
           System.out.println("=============后置通知开始=============");
           System.out.println("=============后置通知结束=============");
       }
   }
   ```

4. afterReturning 返回后通知

   `只有不发生异常才会执行`

   ```java
   @Aspect
   @Component
   public class LogAspect {
     	@Pointcut("within(cn.wsq.aopdemo.service.impl.*ServiceImpl)")
       public void pointcut() {}
       /**
        * 返回后通知 目标方法执行后执行
        */
       @AfterReturning(value = "pointcut()")
       public void logAfterReturning(){
           System.out.println("=============返回后通知开始=============");
           System.out.println("=============返回后通知结束=============");
   	}
   }
   ```

5. afterThrowing 异常通知

   `只有发生异常才会执行`

   ~~~java
   @Aspect
   @Component
   public class LogAspect {
     	@Pointcut("within(cn.wsq.aopdemo.service.impl.*ServiceImpl)")
       public void pointcut() {}
      /**
        * 异常通知 目标方法执行之后执行
        */
       @AfterThrowing(value = "pointcut()")
       public void logAfterThrowing(){
           System.out.println("=============异常通知开始=============");
           System.out.println("=============异常通知结束=============");
       }
   }
   ~~~

### 5.测试

1. 代码不存在异常
    <img src="https://img2020.cnblogs.com/blog/1814997/202007/1814997-20200712231450455-2092333881.png" alt="image-20200712185603345" style="zoom:80%;" />


2. 代码存在常

   修改一下目标方法 ，添加`int a = 10/0;`制造异常

   ```java
   @Override
   public User save(User user) {
       System.out.println("UserServiceImpl.save 添加用户：" + user);
       int a = 10/0;
       return userDao.save(user);
   }
   ```
      <img src="https://img2020.cnblogs.com/blog/1814997/202007/1814997-20200712231504052-2042788495.png" alt="image-20200712185603345" style="zoom:80%;" />

### 6.基于注解的方式指定切入点

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogAnnotation {
    String value() default "";
}
```

```java
@Aspect
@Component
public class AnnotationAspect {
    //指定注解，使用这些注解的方法都会被增强
    @Pointcut("@annotation(cn.wsq.aopdemo.annotation.LogAnnotation)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getDeclaredAnnotation(LogAnnotation.class);
        if (logAnnotation != null) {
            System.out.println("注解的value："+logAnnotation.value());
        }
        Object result = point.proceed();
        return result;
    }
}
```

`我们可以根据注解的参数，来人为的添加我们想要在切面中使用的数据`

复制一个userService的实现类为userServiceImpl2

```java
@Service
public class UserServiceImpl2 implements UserService {

    private final UserDao userDao;

    public UserServiceImpl2(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @LogAnnotation(value="添加用户") //声明自定义注解
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
```
<img src="https://img2020.cnblogs.com/blog/1814997/202007/1814997-20200712231517159-504002682.png" alt="image-20200712185603345" style="zoom:80%;" />
