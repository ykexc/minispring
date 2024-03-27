package com.minis.test;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.RequestMapping;

/**
 * @author mqz
 */
public class HelloWorldBean {

    @Autowired
    private AServiceImpl aservice;


    @RequestMapping("/test")
    public String doTest() {
        aservice.sayHello();
        return "hello world for doGet!";
    }

}
