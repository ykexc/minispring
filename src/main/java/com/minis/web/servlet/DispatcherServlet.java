package com.minis.web.servlet;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.exception.NoSuchBeanDefinitionException;
import com.minis.web.AnnotationConfigWebApplicationContext;
import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;
import com.minis.web.XmlScanComponentHelper;
import com.minis.web.servlet.view.ModelAndView;
import com.minis.web.servlet.view.View;
import com.minis.web.servlet.view.ViewResolver;

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
import java.util.*;

/**
 * @author mqz
 */
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

    private String sContextConfigLocation;

    private WebApplicationContext webApplicationContext;

    private WebApplicationContext parentWebApplicationContext;

    private List<String> controllerNames = new ArrayList<>();

    private Map<String,Object> controllerObjs = new HashMap<>();


    // 存储 controller 的名称与controller类的映射关系
    private Map<String, Class<?>> controllerClasses = new HashMap<>();


    private HandlerMapping handlerMapping;

    private HandlerAdapter handlerAdapter;

    private ViewResolver viewResolver;


    public DispatcherServlet() {
        super();
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.parentWebApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = config.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // 存储需要扫描的 package
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(sContextConfigLocation, this.parentWebApplicationContext);
        refresh();
    }

    protected void refresh() {
        try {
            initController();
        } catch (NoSuchBeanDefinitionException | BeansException | IllegalAccessException e) {
            e.printStackTrace();
        }
        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
        initViewResolvers(this.webApplicationContext);
    }

    protected void initViewResolvers(WebApplicationContext wac) {
        try {
            this.viewResolver = (ViewResolver) wac.getBean(VIEW_RESOLVER_BEAN_NAME);
        } catch (BeansException | NoSuchBeanDefinitionException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        try {
            this.handlerMapping = (HandlerMapping) wac.getBean(HANDLER_MAPPING_BEAN_NAME);
        } catch (BeansException | NoSuchBeanDefinitionException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void initHandlerAdapters(WebApplicationContext wac) {
        try {
            this.handlerAdapter = (HandlerAdapter) wac.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (NoSuchBeanDefinitionException | BeansException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 对实例化的每一个类进行初始化
     */
    protected void initController() throws NoSuchBeanDefinitionException, BeansException, IllegalAccessException {
        this.controllerNames = Arrays.asList(this.webApplicationContext.getBeanDefinitionNames());
        for (String controllerName : this.controllerNames) {
            Class<?> clz;
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
                this.controllerObjs.put(controllerName, this.webApplicationContext.getBean(controllerName));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processRequest = request;
        HandlerMethod handlerMethod = null;
        ModelAndView mv = null;
        handlerMethod = this.handlerMapping.getHandler(processRequest);
        if (handlerMethod == null) return;
        HandlerAdapter adapter = this.handlerAdapter;
        mv = adapter.handle(processRequest, response, handlerMethod);
        render(processRequest, response, mv);
    }

    protected void render(HttpServletRequest request, HttpServletResponse resp, ModelAndView mv) throws Exception {
        if (mv == null) {
            resp.getWriter().flush();
            resp.getWriter().close();
        } else {
            String sTarget = mv.getViewName();
            Map<String, Object> modelMap = mv.getModel();
            View view = resolveViewName(sTarget, modelMap, request);
            if (view == null) {  //如果没有view就404
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                view.render(modelMap, request, resp);
            }

        }
    }

    protected View resolveViewName(String viewName, Map<String, Object> model, HttpServletRequest request) throws Exception {
        if (this.viewResolver != null) {
            return viewResolver.resolveViewName(viewName);
        }
        return null;
    }

}
