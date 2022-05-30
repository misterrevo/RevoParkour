package com.revo.application.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MessageAspect {

    @Before("execution(* com.revo.domain.AreaService.test(..))")
    public void test(JoinPoint joinPoint){
        System.out.println("Works: "+joinPoint.getSourceLocation());
    }
}
