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
public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {

    SimpleBeanFactory beanFactory;


    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource res = new ClassPathXmlResource(fileName);
        SimpleBeanFactory bf = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(res);
        this.beanFactory = bf;

        if (isRefresh) {
            this.beanFactory.refresh();
        }
    }

    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return this.beanFactory.containsBean(beanName);
    }


    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    @Override
    public boolean isSingleton(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }


    @Override
    public void publishEvent(ApplicationEvent event) {

    }
}
