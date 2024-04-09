package com.test.aop.service;

import com.minis.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author mqz
 */
public class MyAfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("----------my interceptor after method call----------");
    }
}
