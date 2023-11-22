package com.digcoin.snapx.core.error;

import java.util.function.Function;

public interface Transformable<T> {

    T getSelf();

    default <R> R into(Function<T, R> translator) {
        return translator.apply(getSelf());
    }

}
