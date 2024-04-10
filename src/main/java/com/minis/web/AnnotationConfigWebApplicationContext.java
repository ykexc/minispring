package com.minis.web;

import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.*;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mqz
 */
public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {


    private WebApplicationContext parentApplicationContext;

    private ServletContext servletContext;

    DefaultListableBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();


    public AnnotationConfigWebApplicationContext(String fileName) {
        this(fileName, null);
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        this.servletContext = this.parentApplicationContext.getServletContext();
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = scanPackages(packageNames);
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        this.beanFactory = bf;
        try {
            this.beanFactory.setParentBeanFactory(this.parentApplicationContext.getBeanFactory());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        loadBeanDefinitions(controllerNames);
        if (true) {
            try {
                refresh();
            } catch (BeansException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalAccessException {
        return this.beanFactory;
    }

    @Override
    protected void registerListeners() {
        ApplicationListener applicationListener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(applicationListener);
    }

    @Override
    protected void initApplicationEventPublisher() {
        ApplicationEventPublisher applicationEventPublisher = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(applicationEventPublisher);
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

    }

    @Override
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        try {
            this.beanFactory.addBeanPostProcessor((BeanPostProcessor) this.getBean("autowiredAnnotationBeanPostProcessor"));
        } catch (NoSuchBeanDefinitionException | BeansException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    protected void finishRefresh() {
        return;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        //将以.分隔的包名换成以/分隔的uri
        try {
            uri = this.getClass().getResource("/" +
                                              packageName.replaceAll("\\.", "/")).toURI();
        } catch (Exception e) {
        }
        File dir = new File(uri);
        //处理对应的文件目录
        for (File file : dir.listFiles()) { //目录下的文件或者子目录
            if (file.isDirectory()) { //对子目录递归扫描
                scanPackage(packageName + "." + file.getName());
            } else { //类文件
                String controllerName = packageName + "."
                                        + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    public void loadBeanDefinitions(List<String> controllerNames) {
        //因为只实现了name注入，感觉这样有点怪
        controllerNames.stream().filter(e -> {
            try {
                return !Class.forName(e).isInterface();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }).forEach(
                controllerName -> this.beanFactory.registerBeanDefinition(controllerName, new BeanDefinition(controllerName, controllerName))
        );


    }

}
