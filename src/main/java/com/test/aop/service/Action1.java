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

    @Override
    public String doAction2() {
        System.out.println("really do action2");
        return "do action2";
    }
}
