package com.minis.web;

import com.minis.beans.factory.exception.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author mqz
 */
public class AnnotationConfigWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {


    private ServletContext servletContext;

    public AnnotationConfigWebApplicationContext(String fileName) throws BeansException, IllegalAccessException {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
