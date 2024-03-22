package com.minis.beans;

import com.minis.core.Resource;
import org.dom4j.Element;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public class XmlBeanDefinitionReader {

    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory beanFactory) {
        this.simpleBeanFactory = beanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            this.simpleBeanFactory.registerBeanDefinition(new BeanDefinition(beanId, beanClassName));
        }
    }

}
