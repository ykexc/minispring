package com.minis.beans.factory.support;

import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;
import com.minis.beans.factory.config.*;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mqz
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    protected List<String> beanDefinitionNames = new ArrayList<>();

    protected final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {
    }


    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (NoSuchBeanDefinitionException | BeansException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }


    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException, BeansException {
        //先尝试直接从容器中获取bean实例
        Object singleton = this.getSingleton(beanName);
        if (null == singleton) {
            //如果为null，再尝试从毛胚实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                //如果连毛胚都没有,则创建bean实例并注册
                System.out.println("get bean null --------------- " + beanName);
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if (beanDefinition != null) {
                    singleton = createBean(beanDefinition);
                    this.registerBean(beanName, singleton);
                    if (singleton instanceof BeanFactoryAware) {
                        ((BeanFactoryAware) singleton).setBeanFactory(this);
                    }
                    //  进行beanpostprocessor操作
                    //  step1: postProcessBeforeInitialization
                    applyBeanPostProcessorsBeforeInitialization(singleton, beanName);

                    //  stpe2: init-method
                    if (beanDefinition.getInitMethodName() != null && !beanDefinition.getInitMethodName().isEmpty()) {
                        invokeInitMethod(beanDefinition, singleton);
                    }
                    //  step3: postProcessorAfterInitialization
                    applyBeanPostProcessorsAfterInitialization(singleton, beanName);
                }
            }
        } else {
            System.out.println("-----------bean exist--------");
        }
        if (singleton instanceof FactoryBean) {
            System.out.println("factory bean -------------- " + beanName + "----------------"+singleton);
            return this.getObjectForBeanInstance(singleton, beanName);
        }
        return singleton;
    }


    protected Object getObjectForBeanInstance(Object singleton, String beanName) {
        if (!(singleton instanceof FactoryBean)) {
            return singleton;
        }
        Object object;
        FactoryBean<?> factory = (FactoryBean<?>) singleton;
        object = getObjectFromFactoryBean(factory, beanName);
        return object;
    }

    @Override
    public Boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getBeanClass();
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        this.beanDefinitionMap.put(name, bd);
        this.beanDefinitionNames.add(name);
        if (!bd.isLazyInit()) {
            try {
                getBean(name);
            } catch (NoSuchBeanDefinitionException | BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }


    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //创建毛坯实例
        Object obj = doCreateBean(beanDefinition);
        //存放到毛坯实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);

        //完善bean, 主要是处理属性
        populateBean(beanDefinition, clz, obj);
        return obj;
    }

    protected abstract Object applyBeanPostProcessorsBeforeInitialization(Object singleton, String beanName) throws BeansException;

    protected abstract Object applyBeanPostProcessorsAfterInitialization(Object obj, String beanName) throws BeansException;

    private void invokeInitMethod(BeanDefinition beanDefinition, Object obj) {
        Class<?> clz = beanDefinition.getBeanClass();
        Method method;
        try {
            method = clz.getMethod(beanDefinition.getInitMethodName());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            method.invoke(obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clz;
        Object obj = null;
        Constructor<?> con;

        try {
            clz = Class.forName(beanDefinition.getClassName());
            //handle constructor
            ConstructorArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
            if (argumentValues != null && !argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try {
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                obj = clz.newInstance();
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(beanDefinition.getId() + " bean created. " + beanDefinition.getClassName() + " : " + obj.toString());

        return obj;
    }

    private void populateBean(BeanDefinition beanDefinition, Class<?> clz, Object obj) {
        handleProperties(beanDefinition, clz, obj);
    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clz, Object obj) {
        //handle properties
        System.out.println("handle properties for bean : " + beanDefinition.getId());
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues != null && !propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) {
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }

                    paramValues[0] = pValue;
                } else { //is ref, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        paramValues[0] = getBean((String) pValue);
                    } catch (NoSuchBeanDefinitionException | BeansException e) {
                        e.printStackTrace();
                    }
                }

                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);

                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }


            }
        }
    }



}
