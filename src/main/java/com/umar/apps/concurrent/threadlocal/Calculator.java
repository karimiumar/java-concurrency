package com.umar.apps.concurrent.threadlocal;

import java.util.HashMap;

public abstract class Calculator {

    private static ThreadLocal<HashMap> results = new ThreadLocal<>() {
        @Override
        protected HashMap initialValue() {
            return new HashMap();
        }
    };

    public Object calculate(Object param) {
        HashMap hm = results.get();
        Object o = hm.get(param);
        if(null != o) {
            return o;
        }
        o = doLocalCalculate(param);
        hm.put(param, o);
        return o;
    }

    protected abstract Object doLocalCalculate(Object param);
}
