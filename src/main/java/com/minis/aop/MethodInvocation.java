package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author mqz
 */
public interface MethodInvocation {

    Method getMethod();

    Object[] getArguments();

    Object getThis();

    Object proceed() throws Throwable;



}
