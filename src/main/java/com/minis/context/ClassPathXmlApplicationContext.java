package com.minis.context;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.beans.exception.NoSuchBeanDefinitionException;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 * 这里使用到了装饰器模式哦
 */
public class ClassPathXmlApplicationContext implements BeanFactory {

    BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        SimpleBeanFactory simpleBeanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(simpleBeanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = simpleBeanFactory;
    }



    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}
