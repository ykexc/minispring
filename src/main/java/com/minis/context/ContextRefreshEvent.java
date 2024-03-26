package com.minis.context;

/**
 * @author mqz
 */
public class ContextRefreshEvent extends ApplicationEvent{


    private static final long serialVersionUID = 1L;

    /**
     * Constructs a prototypical Event.
     *
     * @param arg0 The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextRefreshEvent(Object arg0) {
        super(arg0);
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
