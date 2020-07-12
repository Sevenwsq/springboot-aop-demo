package cn.wsq.aopdemo.aspects;

import cn.wsq.aopdemo.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.SocketHandler;

/**
 * @author Silent
 * @date 2020-7-12 10:31:09
 */
@Aspect
@Component
public class LogAspect {
    /**
     * 声明切入点，可以采用within表达式，execution表达式和annotation注解式声明
     * within(cn.wsq.aopdemo.service.impl.*ServiceImpl)
     * execution(* cn.wsq.aopdemo.service.impl.*ServiceImpl.*(..))
     * annotation(cn.wsq.aopdemo.annotation.LogAnnotation)
     */
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
             * 这里值的注意的是，如果不给proceed方法传参，直接point.proceed()，那么默认是使用原始参数
             * 如果传参了，则使用传入的参数，在此可以对目标方法的参数进行统一包装
             * new Object[]{new User("zs","zs@163.com")}
             **/
            result = point.proceed();
            System.out.println("执行结果："+result);
            System.out.println("=============环绕通知正常结束=============");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("=============环绕通知异常结束=============");
        }
        //返回目标方法的返回值,我们在返回之前可以做一些特定的操作，比如统一剔除某个字段
        return result;
    }

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
    /**
     * 后置通知 目标方法执行之后执行，包括异常和返回后通知
     */
    @After(value = "pointcut()")
    public void logAfter(){
        System.out.println("=============后置通知开始=============");
        System.out.println("=============后置通知结束=============");
    }
    /**
     * 返回后通知 目标方法执行后执行
     */
    @AfterReturning(value = "pointcut()")
    public void logAfterReturning(){
        System.out.println("=============返回后通知开始=============");
        System.out.println("=============返回后通知结束=============");
    }
    /**
     * 异常通知 目标方法执行之后执行
     */
    @AfterThrowing(value = "pointcut()")
    public void logAfterThrowing(){
        System.out.println("=============异常通知开始=============");
        System.out.println("=============异常通知结束=============");
    }
}
