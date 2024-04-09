package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author mqz
 */
public interface MethodBeforeAdvice extends BeforeAdvice{


    void before(Method method, Object[] args, Object target) throws Throwable;

}
