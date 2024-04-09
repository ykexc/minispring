package com.minis.aop;

/**
 * @author mqz
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
