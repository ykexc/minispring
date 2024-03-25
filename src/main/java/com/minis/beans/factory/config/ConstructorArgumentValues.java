package com.minis.beans.factory.config;

import java.util.*;

/**
 * @author mqz
 * @version 1.0
 * @since 1.0
 */
public class ConstructorArgumentValues {

    private final Map<Integer, ConstructorArgumentValue> indexedArgumentValues = new HashMap<>(0);

    private final List<ConstructorArgumentValue> genericConstructorArgumentValues = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    private void addArgumentValue(Integer key, ConstructorArgumentValue newValue) {
        indexedArgumentValues.put(key, newValue);
    }

    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        this.genericConstructorArgumentValues.add(constructorArgumentValue);
    }

    public boolean hasIndexedArgumentValue(int index) {
        return this.indexedArgumentValues.containsKey(index);
    }

    public void addGenericArgumentValue(Object value, String type) {
        this.genericConstructorArgumentValues.add(new ConstructorArgumentValue(type, value));
    }

    private void addGenericArgumentValue(ConstructorArgumentValue newValue) {
        if (newValue.getName() != null) {
            this.genericConstructorArgumentValues.removeIf(currentValue -> newValue.getName().equals(currentValue.getName()));
        }
        this.genericConstructorArgumentValues.add(newValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        ConstructorArgumentValue constructorArgumentValue = this.genericConstructorArgumentValues.get(index);
        return constructorArgumentValue;
    }

    public ConstructorArgumentValue getGenericArgumentValue(String requiredName) {
        for (ConstructorArgumentValue valueHolder : this.genericConstructorArgumentValues) {
            if (valueHolder.getName() != null && (!valueHolder.getName().equals(requiredName))) {
                continue;
            }
            return valueHolder;
        }
        return null;
    }

    public int getArgumentCount() {
        return this.genericConstructorArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericConstructorArgumentValues.isEmpty();
    }

}
