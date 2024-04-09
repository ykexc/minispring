package com.minis.aop;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;
import com.minis.util.ClassUtils;


/**
 * @author mqz
 */
public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {


    private BeanFactory beanFactory;

    private AopProxyFactory aopProxyFactory;

    private String interceptorName;

    private String targetName;

    private Object target;

    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();


    private Object singletonInstance;

    private PointcutAdvisor advisor;


    private synchronized void initializeAdvisor() {
        Object advice;
        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (NoSuchBeanDefinitionException | BeansException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.advisor = (PointcutAdvisor) advice;

    }

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public AopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }

    protected AopProxy createAopProxy() {
        System.out.println("----------createAopProxy for :"+target+"--------");
        return getAopProxyFactory().createAopProxy(target, this.advisor);
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object getObject() throws Exception {
        initializeAdvisor();
        return getSingletonInstance();
    }

    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        System.out.println("----------return proxy for :"+this.singletonInstance+"--------"+this.singletonInstance.getClass());

        return this.singletonInstance;
    }

    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
