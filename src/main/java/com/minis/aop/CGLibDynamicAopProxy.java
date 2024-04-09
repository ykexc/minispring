package com.minis.aop;

import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;

import java.lang.reflect.Method;

import java.util.Arrays;

/**
 * @author mqz
 */
public class CGLibDynamicAopProxy implements AopProxy, InvocationHandler {


    Object target;

    Advisor advisor;

    public CGLibDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }


    @Override
    public Object getProxy() {
        System.out.println("---------start cglib proxy-----------");
        System.out.println("----------Proxy new psroxy instance for  ---------" + target);
        System.out.println("----------Proxy new psroxy instance  classloader ---------" + JdkDynamicAopProxy.class.getClassLoader());
        System.out.println("----------Proxy new psroxy instance  interfaces  ---------" + Arrays.toString(target.getClass().getInterfaces()));
        Object obj = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        System.out.println("----------Proxy new psroxy instance created r ---------" + obj);
        return obj;
    }




    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("doAction")) {
            Class<?> targetClass = target != null ? target.getClass() : null;
            MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
            MethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
            return interceptor.invoke(methodInvocation);
        }
        return null;
    }

}
