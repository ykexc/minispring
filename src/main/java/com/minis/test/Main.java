package com.minis.test;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

/**
 * @author mqz
 */
public class Main {

    public static void main( String[] args ) throws LifecycleException, ServletException {
        System.out.println( "Hello World!" );
        Tomcat tomcat = new Tomcat();
        String webappDirLocation = "src/main/WebApp";
        StandardContext context = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        Connector connector = new Connector();
        connector.setPort(8000);
        tomcat.setConnector(connector);
        tomcat.start();
        tomcat.getServer().await();
    }

}
