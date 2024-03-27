package com.minis.web;

import com.minis.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author mqz
 */
public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);


}
