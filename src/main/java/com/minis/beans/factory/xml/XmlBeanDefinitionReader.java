package com.minis.beans.factory.xml;

import com.minis.beans.factory.config.*;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public class XmlBeanDefinitionReader {

    AbstractBeanFactory bf;

    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.bf = beanFactory;
    }

    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element) res.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String initMethod = element.attributeValue("init-method");

            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
            beanDefinition.setInitMethodName(initMethod);

            //get constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(pType, pName, pValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor

            //handle properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.isEmpty()) {
                    pV = pValue;
                } else if (pRef != null && !pRef.isEmpty()) {
                    pV = pRef;
                    isRef = true;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            String[] refsArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refsArray);
            try {
                beanDefinition.setBeanClass(Class.forName(beanClassName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            this.bf.registerBeanDefinition(beanID, beanDefinition);
            //end of handle properties

        }
    }

}
