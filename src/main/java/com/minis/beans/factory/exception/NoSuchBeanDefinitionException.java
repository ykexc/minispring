package com.minis.beans.factory.exception;

/**
 * @author mqz
 */
public class NoSuchBeanDefinitionException extends Exception{

    public NoSuchBeanDefinitionException(String msg) {
        super(msg);
    }

}
