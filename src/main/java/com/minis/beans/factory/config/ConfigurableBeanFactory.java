package com.minis.beans.factory.config;

import com.minis.beans.factory.BeanFactory;

/**
 * @author mqz
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";


    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws IllegalAccessException;

    int getBeanPostProcessorCount() throws IllegalAccessException;

    void registerDependentBean(String beanName, String dependentBeanName) throws IllegalAccessException;

    String[] getDependentBeans(String beanName) throws IllegalAccessException;

    String[] getDependenciesForBean(String beanName) throws IllegalAccessException;
}
