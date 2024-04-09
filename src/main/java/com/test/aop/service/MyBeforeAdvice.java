package com.test.aop.service;

import com.minis.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author mqz
 */
public class MyBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("----------my interceptor before method call----------");
    }
}
