package com.minis.core.env;

/**
 * @author mqz
 */
public interface Environment {

    String[] getActiveProfiles();

    String[] getDefaultProfiles();

    boolean acceptsProfiles(String ...profiles);

}
