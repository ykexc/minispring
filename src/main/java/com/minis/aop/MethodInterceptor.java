package com.minis.aop;

/**
 * @author mqz
 */
public interface MethodInterceptor extends Interceptor{


    Object invoke(MethodInvocation invocation) throws Throwable;
}
