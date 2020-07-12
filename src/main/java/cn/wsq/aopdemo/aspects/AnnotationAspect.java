package cn.wsq.aopdemo.aspects;

import cn.wsq.aopdemo.annotation.LogAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Silent
 * @date 2020-7-12 17:44:09
 * @description 这个是基于注解的
 */
@Aspect
@Component
public class AnnotationAspect {
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
