package com.test.aop.service;

/**
 * @author mqz
 */
public class Action1 implements IAction{
    @Override
    public String doAction() {
        System.out.println("really do action");
        return "do action";
    }
}
