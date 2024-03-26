package com.minis.beans.factory;

import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;

import java.util.Map;

/**
 * @author mqz
 */
public interface ListableBeanFactory extends BeanFactory{

    boolean containsBeanDefinition(String beanName) throws IllegalAccessException;

    int getBeanDefinitionCount() throws IllegalAccessException;

    String[] getBeanDefinitionNames() throws IllegalAccessException;

    String[] getBeanNamesForType(Class<?> type) throws IllegalAccessException;

    <T> Map<String, T> getBeansOfTYpe(Class<T> type) throws BeansException, NoSuchBeanDefinitionException, IllegalAccessException;

}
