package com.minis.web;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mqz
 */
public class DispatcherServlet extends HttpServlet {

    private String sContextConfigLocation;

    private WebApplicationContext webApplicationContext;

    private static final long serialVersionUID = 1L;

    // 存储需要扫描的 package
    private List<String> packageNames = new ArrayList<>();
    // 存储 controller 的名称与controller对象的映射关系
    private Map<String, Object> controllerObjs = new HashMap<>();

    // 存储 controller 的名称
    private List<String> controllerNames = new ArrayList<>();
    // 存储 controller 的名称与controller类的映射关系
    private Map<String, Class<?>> controllerClasses = new HashMap<>();

    // 保存自定义的@RequestMapping名称——url的列表
    private List<String> urlMappingNames = new ArrayList<>();

    // 保存 URL 名称与对象的映射关系
    private Map<String, Object> mappingObjs = new HashMap<>();
    // 保存 URL 名称与方法的映射关系，先取出对象，再取出方法
    private Map<String, Method> mappingMethods = new HashMap<>();


    public DispatcherServlet() {
        super();
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.webApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = config.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        refresh();
    }

    protected void refresh() {
        try {
            initController();
        } catch (NoSuchBeanDefinitionException | BeansException e) {
            e.printStackTrace();
        }

        initMapping();
    }

    /**
     * 对实例化的每一个类进行初始化
     */
    protected void initController() throws NoSuchBeanDefinitionException, BeansException {
        this.controllerNames = scanPackages(this.packageNames);
        for (String controllerName : this.controllerNames) {
            Object obj = null;
            Class<?> clz = null;
            //  根据类名加载对象
            try {
                clz = Class.forName(controllerName);
                this.controllerClasses.put(controllerName, clz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (clz.isInterface()) continue;
            //实例化
            try {
                obj = clz.getDeclaredConstructor().newInstance();
                populateBean(obj, controllerName);
                this.controllerObjs.put(controllerName, obj);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    protected Object populateBean(Object bean, String beanName) throws NoSuchBeanDefinitionException, BeansException, IllegalAccessException {
        Object result = bean;
        Class<?> clz = bean.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                String fieldName = field.getName();
                Object autowiredObj = this.webApplicationContext.getBean(fieldName);
                try {
                    field.setAccessible(true);
                    field.set(bean, autowiredObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private void initMapping() {
        for (String controllerName : this.controllerNames) {
            Object obj = this.controllerObjs.get(controllerName);
            Class<?> clz = this.controllerClasses.get(controllerName);
            Method[] allMethods = clz.getDeclaredMethods();
            for (Method method : allMethods) {
                boolean existAnnotation = method.isAnnotationPresent(RequestMapping.class);
                if (existAnnotation) {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String urlMapping = annotation.value();
                    this.urlMappingNames.add(urlMapping);
                    this.mappingObjs.put(urlMapping, obj);
                    this.mappingMethods.put(urlMapping, method);

                }
            }
        }
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if (!this.urlMappingNames.contains(servletPath)) {
            return;
        }
        Object obj = null;
        Object objResult = null;
        try {
            Method method = mappingMethods.get(servletPath);
            obj = mappingObjs.get(servletPath);
            objResult = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.getWriter().write(objResult.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
