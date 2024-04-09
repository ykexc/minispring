package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author mqz
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass);

}
