package com.parent.owm.annotations.scanners;


import com.chaining.Chain;
import com.parent.owm.annotations.LayoutId;

import io.reactivex.functions.Function;

public class LayoutIdScanner implements Function<Object, Integer> {

    @Override
    public Integer apply(Object o) {
        return Chain.optional(o)
                .map(Object::getClass)
                .map(clazz -> clazz.getAnnotation(LayoutId.class))
                .map(LayoutId::value)
                .defaultIfEmpty(0)
                .call();
    }
}
