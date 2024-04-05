package com.minis.test.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.AServiceImpl;
import com.minis.web.RequestMapping;
import com.test.User;

/**
 * @author mqz
 */
public class HelloWorldBean {






    @Autowired
    private AServiceImpl aservice;


    //curl http://localhost:8000/test?name=xxx&gender=xxx
    @RequestMapping("/test")
    public String doTest(User user) {
        System.out.println(user.getName() + " " + user.getGender());
        aservice.sayHello();
        return "hello world for doGet!";
    }

}
