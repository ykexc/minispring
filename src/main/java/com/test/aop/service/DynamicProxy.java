package com.test.aop.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author mqz
 */
public class DynamicProxy<T> {

    private T subject = null;

    public DynamicProxy(T subject) {
        this.subject = subject;
    }

    public T getProxy() {
        return (T) Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(), subject.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("doAction")) {
                    System.out.println("before doAction");
                    return method.invoke(subject, args);
                }
                return null;
            }
        });
    }

    public static void main(String[] args) {
        Action1 action1 = new Action1();
        DynamicProxy<IAction> dynamicProxy = new DynamicProxy<>(action1);
        IAction proxy = dynamicProxy.getProxy();
        proxy.doAction();
    }

}
