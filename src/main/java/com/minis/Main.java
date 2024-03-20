package com.minis;

import com.minis.beans.exception.NoSuchBeanDefinitionException;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.test.AService;

/**
 * @author mqz
 */
public class Main {

    public static void main(String[] args) throws NoSuchBeanDefinitionException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) context.getBean("aservice");
        aService.sayHello();
    }

}
