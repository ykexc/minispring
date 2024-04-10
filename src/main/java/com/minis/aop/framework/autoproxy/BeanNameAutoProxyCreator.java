package com.minis.aop.framework.autoproxy;

import com.minis.aop.*;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.exception.BeansException;
import com.minis.util.PatternMatchUtils;

/**
 * @author mqz
 */
public class BeanNameAutoProxyCreator implements BeanPostProcessor {

    String pattern;

    private BeanFactory beanFactory;

    private AopProxyFactory aopProxyFactory;

    private String interceptorName;

    private PointcutAdvisor advisor;

    public BeanNameAutoProxyCreator() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setAdvisor(PointcutAdvisor advisor) {
        this.advisor = advisor;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(" try to create proxy for : " + beanName);
        if (isMatch(beanName, this.pattern)) {
            System.out.println(beanName + "bean name matched, " + this.pattern + " create proxy for " + bean);
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setInterceptorName(this.interceptorName);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            bean = proxyFactoryBean;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    protected AopProxy createAopProxy(Object target) {
        return aopProxyFactory.createAopProxy(target, this.advisor);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected boolean isMatch(String beanName, String mappedName) {
        System.out.println(" match?" + beanName + ":" + mappedName);
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

}
