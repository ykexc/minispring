package com.minis.web.servlet;

import com.minis.beans.factory.exception.BeansException;
import com.minis.context.ApplicationContext;
import com.minis.web.*;
import com.minis.web.http.HttpMessageConverter;
import com.minis.web.servlet.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author mqz
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter, ApplicationContextAware{

    ApplicationContext applicationContext;

    WebBindingInitializer webBindingInitializer;

    HttpMessageConverter messageConverter;



    public RequestMappingHandlerAdapter() {}

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
        ModelAndView mv = null;
        try {
            mv = invokeHandlerMethod(request, response, handlerMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse resp, HandlerMethod handlerMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        for (Parameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getType();
            if (type == HttpServletRequest.class) {
                methodParamObjs[i] = request;
                continue;
            }
            if (type == HttpServletResponse.class) {
                methodParamObjs[i] = resp;
                continue;
            }

            Object methodParamObj = methodParameter.getType().newInstance();
            //  给这个参数创建WebDataBinder
            WebDataBinder binder = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            webBindingInitializer.initBinder(binder);
            binder.bind(request);
            methodParamObjs[i++] = methodParamObj;
        }
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        Class<?> returnType = invocableMethod.getReturnType();
        ModelAndView mav = null;
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)) {
            this.messageConverter.write(returnObj, resp);
        } else if (returnType == void.class) {
            // pass
        } else {
            // return view
            if (returnObj instanceof ModelAndView) {
                mav = (ModelAndView) returnObj;
            } else if (returnObj instanceof String) {
                String sTarget = (String) returnObj;
                mav = new ModelAndView();
                mav.setViewName(sTarget);
            }
        }
        return mav;
    }


    public void setWebBindingInitializer(WebBindingInitializer initializer) {
        this.webBindingInitializer = initializer;
    }

    public HttpMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public WebBindingInitializer getInitializer() {
        return webBindingInitializer;
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
