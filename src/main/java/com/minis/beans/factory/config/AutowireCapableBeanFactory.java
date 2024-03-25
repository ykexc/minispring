package com.minis.beans.factory.config;

import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.support.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mqz
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {


    private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessCount() {
        return this.beanPostProcessors.size();
    }

    public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {return this.beanPostProcessors;}


    @Override
    protected Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            beanProcessor.setBeanFactory(this);
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (null == result) return null;
        }
        return result;
    }

    @Override
    protected Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) throws BeansException{
        Object result = existingBean;
        for (AutowiredAnnotationBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (null == result) return result;
        }

        return result;
    }
}
