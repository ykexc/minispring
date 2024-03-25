package com.minis.beans.factory;

import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public interface BeanFactory {

    Object getBean(String beanName) throws NoSuchBeanDefinitionException, BeansException;

    Boolean containsBean(String beanName);

    //void registerBean(String beanName, Object obj);


    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);

}
