package com.minis.beans;

/**
 * @author mqz
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    Boolean containsSingleton(String beanName);

    String[] getSingletonNames();

}
