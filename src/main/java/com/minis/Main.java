package com.minis;

import com.minis.beans.exception.BeansException;
import com.minis.beans.exception.NoSuchBeanDefinitionException;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.test.AService;

/**
 * @author mqz
 */
public class Main {

    public static void main(String[] args) throws NoSuchBeanDefinitionException, ClassNotFoundException {

//        Class<?> aClass = Class.forName("com.minis.test.AServiceImpl");

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService;
        try {
            aService = (AService)ctx.getBean("aservice");
            aService.sayHello();
        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
        }
    }

}
