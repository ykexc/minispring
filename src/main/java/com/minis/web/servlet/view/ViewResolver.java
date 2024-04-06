package com.minis.web.servlet.view;

/**
 * @author mqz
 */
public interface ViewResolver {


    View resolveViewName(String viewName) throws Exception;

}
