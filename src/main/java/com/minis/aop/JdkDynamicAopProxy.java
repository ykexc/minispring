package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author mqz
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {


    Object target;

    Advisor advisor;

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        System.out.println("---------jdk proxy-----------");
        System.out.println("----------Proxy new psroxy instance for  ---------" + target);
        System.out.println("----------Proxy new psroxy instance  classloader ---------" + JdkDynamicAopProxy.class.getClassLoader());
        System.out.println("----------Proxy new psroxy instance  interfaces  ---------" + Arrays.toString(target.getClass().getInterfaces()));
        Object obj = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        System.out.println("----------Proxy new psroxy instance created r ---------" + obj);
        return obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //采用命令模式，将参数以对象形式传递

        if (method.getName().equals("doAction")) {
            Class<?> targetClass = target != null ? target.getClass() : null;
            MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
            MethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
            return interceptor.invoke(methodInvocation);
        }
        return null;
    }
}
