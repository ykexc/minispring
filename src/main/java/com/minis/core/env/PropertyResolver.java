package com.minis.core.env;

/**
 * @author mqz
 */
public interface PropertyResolver {
    boolean containsProperty(String key);

    String getProperty(String key);

    String getProperty(String key, String defaultValue);

    <T> T getProperty(String key, Class<T> targetType);

    <T> T getProperty(String key, Class<T> targetType, T defaultValue);

    <T> Class<T> getPropertyAsClass(String key, Class<T> targetType);

    String getRequiredProperty(String key) throws IllegalAccessException;

    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalAccessException;

    String resolvePlaceholders(String text);

    String resolveRequiredPlaceholders(String text) throws IllegalAccessException;
}
