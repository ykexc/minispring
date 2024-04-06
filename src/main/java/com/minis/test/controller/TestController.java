package com.minis.test.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.AServiceImpl;
import com.minis.web.RequestMapping;
import com.minis.web.ResponseBody;
import com.minis.web.servlet.view.ModelAndView;
import com.test.User;

/**
 * @author mqz
 */
public class TestController {






    @Autowired
    private AServiceImpl aservice;


    //curl http://localhost:8000/test?name=xxx&gender=xxx
    @RequestMapping("/test")
    @ResponseBody
    public User doTest(User user) {
        System.out.println(user.getName() + " " + user.getGender());
        aservice.sayHello();
        return user;
    }


    @RequestMapping("/test5")
    public ModelAndView doTest5(User user) {
        ModelAndView mav = new ModelAndView("test","msg",user.getName());
        return mav;
    }
    @RequestMapping("/test6")
    public String doTest6(User user) {
        return "error";
    }

}
