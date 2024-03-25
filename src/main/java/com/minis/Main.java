package com.minis;

import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;
import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.test.AService;
import com.minis.test.BaseService;

/**
 * @author mqz
 */
public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService;
        BaseService bService;
        try {
            //aService = (AService)ctx.getBean("aservice");
            //aService.sayHello();

            bService = (BaseService)ctx.getBean("baseservice");
            bService.sayHello();
        } catch (BeansException | NoSuchBeanDefinitionException e) {
            e.printStackTrace();
        }
    }

}
