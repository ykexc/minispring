package com.minis.test;

import com.minis.web.RequestMapping;

/**
 * @author mqz
 */
public class HelloWorldBean {



    @RequestMapping("/test")
    public String doTest() { return "hello world for doGet!"; }

}
