package com.minis.web.servlet;

import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author mqz
 */
public class RequestMappingHandlerMapping implements HandlerMapping{


    WebApplicationContext wac;

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public RequestMappingHandlerMapping(WebApplicationContext wac) {
        this.wac = wac;
        initMapping();
    }



    protected void initMapping() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames;
        try {
            controllerNames = this.wac.getBeanDefinitionNames();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
                obj = this.wac.getBean(controllerName);
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
                }
            }
        }
    }

    public HandlerMethod getHandler(HttpServletRequest request) {
        String sPath = request.getServletPath();
        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) return null;
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        return new HandlerMethod(method, obj);
    }
}
