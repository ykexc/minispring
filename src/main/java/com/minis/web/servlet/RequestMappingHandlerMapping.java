package com.minis.web.servlet;

import com.minis.beans.factory.exception.BeansException;
import com.minis.context.ApplicationContext;
import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author mqz
 */
public class RequestMappingHandlerMapping implements HandlerMapping, ApplicationContextAware{


    ApplicationContext applicationContext;

    private MappingRegistry mappingRegistry;



    public RequestMappingHandlerMapping() {}


    protected void initMappings() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames;
        try {
            controllerNames = this.applicationContext.getBeanDefinitionNames();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
                obj = this.applicationContext.getBean(controllerName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                if (isRequestMapping) {
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    this.mappingRegistry.getUrlMappingNames().add(urlMapping);
                    this.mappingRegistry.getMappingObjs().put(urlMapping, obj);
                    this.mappingRegistry.getMappingMethods().put(urlMapping, method);
                    this.mappingRegistry.getMappingMethodNames().put(urlMapping, method.getName());
                    this.mappingRegistry.getMappingClasses().put(urlMapping, clz);
                }
            }
        }
    }

    public HandlerMethod getHandler(HttpServletRequest request) {

        if (this.mappingRegistry == null) { //to do initialization
            this.mappingRegistry = new MappingRegistry();
            initMappings();
        }


        String sPath = request.getServletPath();
        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) return null;
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        return new HandlerMethod(method, obj);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
