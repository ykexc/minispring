package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author mqz
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    protected final Object target;

    protected final Method method;

    protected Object[] arguments;

    protected final Object proxy;

    private Class<?> targetClass;

    protected ReflectiveMethodInvocation(
            Object proxy, Object target, Method method, Object[] arguments,
            Class<?> targetClass) {

        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
    }

    public final Object getProxy() {
        return this.proxy;
    }

    public final Object getThis() {
        return this.target;
    }

    public final Method getMethod() {
        return this.method;
    }

    public final Object[] getArguments() {
        return this.arguments;
    }

    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Object proceed() throws Throwable {
        return this.method.invoke(this.target, this.arguments);
    }
}
