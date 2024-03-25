package com.minis.beans.factory.support;

import com.minis.beans.factory.config.BeanDefinition;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String name, BeanDefinition bd);

    void removeBeanDefinition(String name);

    BeanDefinition getBeanDefinition(String name);

    boolean containsBeanDefinition(String name);

}
