package com.minis.web.servlet;

import com.minis.web.WebApplicationContext;
import com.minis.web.WebDataBinder;
import com.minis.web.WebDataBinderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author mqz
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter{

    WebApplicationContext wac;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
    }



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
        try {
            invokeHandlerMethod(request, response, handlerMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse resp, HandlerMethod handlerMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        for (Parameter methodParameter : methodParameters) {
            Object methodParamObj = methodParameter.getType().newInstance();
            //  给这个参数创建WebDataBinder
            WebDataBinder binder = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            binder.bind(request);
            methodParamObjs[i++] = methodParamObj;
        }
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        resp.getWriter().append(returnObj.toString());
    }

}
