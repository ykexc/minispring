package com.minis.aop;

/**
 * @author mqz
 */
public interface AopProxyFactory {

    AopProxy createAopProxy(Object target);

}
