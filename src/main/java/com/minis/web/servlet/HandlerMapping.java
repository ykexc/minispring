package com.minis.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mqz
 */
public interface HandlerMapping {

    HandlerMethod getHandler(HttpServletRequest request) throws Exception;


}
