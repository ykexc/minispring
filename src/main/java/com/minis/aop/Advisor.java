package com.minis.aop;

/**
 * @author mqz
 */
public interface Advisor {

    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);

}
