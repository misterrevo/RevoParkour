package com.revo.application.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AreaServiceMessenger {
    @Before("execution(* com.revo.domain.AreaService.test(..))")
    public void test(JoinPoint joinPoint){
        System.out.println("Works: "+joinPoint.getSourceLocation());
    }
}
