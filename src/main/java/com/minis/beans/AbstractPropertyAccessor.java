package com.minis.beans;

import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;

/**
 * @author mqz
 */
public abstract class AbstractPropertyAccessor extends PropertyEditorRegistrySupport{

    PropertyValues pvs;

    public AbstractPropertyAccessor() {
        super();
    }

    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    public abstract void setPropertyValue(PropertyValue pv) ;
}
