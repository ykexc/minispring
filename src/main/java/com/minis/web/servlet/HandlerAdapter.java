package com.minis.web.servlet;

import com.minis.web.servlet.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mqz
 */
public interface HandlerAdapter {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler);

}
