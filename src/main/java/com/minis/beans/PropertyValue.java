package com.minis.beans;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public class PropertyValue {

    private final String name;

    private final Object value;

    private final String type;

    public PropertyValue(String type, String name, Object value) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }


    public String getType() {
        return type;
    }
}
