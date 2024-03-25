package com.minis.context;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 * 这里使用到了装饰器模式哦
 */
public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {

    AbstractBeanFactory beanFactory;


    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource res = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(res);
        this.beanFactory = beanFactory;

        if (isRefresh) {
            refresh();
        }
    }

    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException, BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return this.beanFactory.containsBean(beanName);
    }


    public void refresh() {
        // Register bean processors that intercept bean creation.
        registerBeanPostProcessors((AutowireCapableBeanFactory) this.beanFactory);
        // Initialize other special beans in specific context subclasses.
        onRefresh();
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    private void onRefresh() {
        this.beanFactory.refresh();
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
