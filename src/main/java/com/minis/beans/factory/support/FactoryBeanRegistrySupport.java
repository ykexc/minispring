package com.minis.beans.factory.support;

import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.exception.BeansException;

/**
 * @author mqz
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
        return factoryBean.getObjectType();
    }


    protected Object getObjectFromFactoryBean(final FactoryBean<?> factoryBean, String beanName) {
        Object obj = doGetObjectFromFactoryBean(factoryBean, beanName);
        try {
            obj = postProcessObjectFactoryBean(obj, beanName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public Object doGetObjectFromFactoryBean(final FactoryBean<?> factoryBean, String beanName) {
        Object obj = null;
        try {
            obj = factoryBean.getObject();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    protected Object postProcessObjectFactoryBean(Object object, String beanName) throws BeansException {
        return object;
    }

    protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws BeansException {

        if (!(beanInstance instanceof FactoryBean)) {
            throw new BeansException(
                    "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
        }
        return (FactoryBean<?>) beanInstance;

    }

}
