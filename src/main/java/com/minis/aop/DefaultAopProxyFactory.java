package com.minis.aop;

/**
 * @author mqz
 */
public class DefaultAopProxyFactory implements AopProxyFactory {


    @Override
    public AopProxy createAopProxy(Object target, Advisor advisor) {
        return new CGLibDynamicAopProxy(target, advisor);
    }
}
