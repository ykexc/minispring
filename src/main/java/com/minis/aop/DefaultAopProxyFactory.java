package com.minis.aop;

/**
 * @author mqz
 */
public class DefaultAopProxyFactory implements AopProxyFactory {
    @Override
    public AopProxy createAopProxy(Object target) {
        return new CGLibDynamicAopProxy(target);
    }
}
