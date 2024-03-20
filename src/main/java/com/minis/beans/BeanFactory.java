package com.minis.beans;

import com.minis.beans.exception.NoSuchBeanDefinitionException;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public interface BeanFactory {

    Object getBean(String beanName) throws NoSuchBeanDefinitionException;

    void registerBeanDefinition(BeanDefinition beanDefinition);

}
