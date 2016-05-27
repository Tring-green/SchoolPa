package com.testing.testfunction.Lib.Callback;

import java.lang.reflect.ParameterizedType;

/**
 * Created by admin on 2016/5/27.
 */
public abstract class ObjectCallback<T> {

    private final Class<T> clazz;

    protected ObjectCallback() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) type.getActualTypeArguments()[0];
    }

    public Class<T> getDataClass() {
        return clazz;

    }

    abstract public void onSuccess(T data);

    abstract public void onFailure(int errorCode, String errorMessage);
}
