package com.web.cloudtube.core.util;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class CloseableReflectionMethodHandler implements AutoCloseable {
    private WeakReference<Method> m;

    public CloseableReflectionMethodHandler(String method, Class<?> clazz)
            throws NoSuchMethodException {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method _m : methods) {
            if(_m.getName().equals(method)) {
                this.m = new WeakReference<>(_m);
                break;
            }
        }
    }

    public Method getMethod() {
        return this.m.get();
    }

    @Override
    public void close() throws Exception {
        m.clear();
    }
}
