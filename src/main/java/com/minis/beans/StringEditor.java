package com.minis.beans;

import java.text.NumberFormat;

import com.minis.util.NumberUtils;
import com.minis.util.StringUtils;

public class StringEditor implements PropertyEditor {
    private Class<String> strClass;
    private String strFormat;
    private boolean allowEmpty;
    private Object value;

    public StringEditor(Class<String> strClass,
                        boolean allowEmpty) throws IllegalArgumentException {
        this(strClass, "", allowEmpty);
    }

    public StringEditor(Class<String> strClass,
                        String strFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.strClass = strClass;
        this.strFormat = strFormat;
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text) {
        setValue(text);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getAsText() {
        return value.toString();
    }

    public Object getValue() {
        return this.value;
    }
}