package com.minis.beans;

import com.minis.beans.exception.NoSuchBeanDefinitionException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public class SimpleBeanFactory implements BeanFactory {

    private final Map<String, Object> singletons = new HashMap<>();

    private final List<String> beanNames = new ArrayList<>();

    private final List<BeanDefinition> beanDefinitions = new ArrayList<>();

    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
        //先尝试直接拿bean实例
        Object singleton = singletons.get(beanName);
        //如果没有就创建新的
        if (singleton == null) {
            int idx = beanNames.indexOf(beanName);
            if (idx == -1) throw new NoSuchBeanDefinitionException();
            else {
                BeanDefinition beanDefinition = beanDefinitions.get(idx);
                try {
                    singleton = Class.forName(beanDefinition.getClassName()).getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                         InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                singletons.put(beanDefinition.getId(), singleton);
            }
        }
        return singleton;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanNames.add(beanDefinition.getId());
        this.beanDefinitions.add(beanDefinition);
    }
}
