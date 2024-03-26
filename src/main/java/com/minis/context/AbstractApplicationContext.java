package com.minis.context;

import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableBeanFactory;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;
import com.minis.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author mqz
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private Environment environment;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    private long startupDate;

    private final AtomicBoolean active = new AtomicBoolean();

    private final AtomicBoolean closed = new AtomicBoolean();

    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException, BeansException, IllegalAccessException {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Boolean containsBean(String beanName) throws IllegalAccessException {
        return getBeanFactory().containsBean(beanName);
    }

    @Override
    public boolean isPrototype(String name) throws IllegalAccessException {
        return getBeanFactory().isPrototype(name);
    }

    @Override
    public boolean isSingleton(String name) throws IllegalAccessException {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public Class<?> getType(String name) throws IllegalAccessException {
        return getBeanFactory().getType(name);
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    public void refresh() throws BeansException, IllegalAccessException {

        postProcessBeanFactory(getBeanFactory());

        registerBeanPostProcessors(getBeanFactory());

        initApplicationEventPublisher();

        onRefresh();

        registerListeners();

        finishRefresh();


    }

    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalAccessException;

    abstract void registerListeners();

    abstract void initApplicationEventPublisher();

    abstract void postProcessBeanFactory(ConfigurableListableBeanFactory bf);

    abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory bf);

    abstract void onRefresh();

    abstract void finishRefresh();

    @Override
    public void registerSingleton(String beanName, Object singletonObject) throws IllegalAccessException {
        getBeanFactory().registerSingleton(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) throws IllegalAccessException {
        return getBeanFactory().getSingleton(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) throws IllegalAccessException {
        return getBeanFactory().containsSingleton(beanName);
    }

    @Override
    public String[] getSingletonNames() throws IllegalAccessException {
        return getBeanFactory().getSingletonNames();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) throws IllegalAccessException {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() throws IllegalAccessException {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() throws IllegalAccessException {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) throws IllegalAccessException {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfTYpe(Class<T> type) throws BeansException, NoSuchBeanDefinitionException, IllegalAccessException {
        return getBeanFactory().getBeansOfTYpe(type);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws IllegalAccessException {
        getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() throws IllegalAccessException {
        return getBeanFactory().getBeanPostProcessorCount();
    }


    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) throws IllegalAccessException {
        getBeanFactory().registerDependentBean(beanName, dependentBeanName);
    }

    @Override
    public String[] getDependentBeans(String beanName) throws IllegalAccessException {
        return getBeanFactory().getDependentBeans(beanName);
    }

    @Override
    public String[] getDependenciesForBean(String beanName) throws IllegalAccessException {
        return getBeanFactory().getDependenciesForBean(beanName);
    }

    @Override
    public String getApplicationName() {
        return "app";
    }

    @Override
    public long getStartupDate() {
        return this.startupDate;
    }



    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


}
