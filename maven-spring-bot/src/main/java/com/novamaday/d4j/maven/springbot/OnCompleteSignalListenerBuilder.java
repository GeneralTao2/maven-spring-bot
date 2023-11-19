package com.novamaday.d4j.maven.springbot;

import reactor.core.observability.DefaultSignalListener;
import reactor.core.observability.SignalListener;

import java.util.function.Supplier;


public class OnCompleteSignalListenerBuilder {
    public static  <T> Supplier<SignalListener<T>> of(Runnable runnable) {
        return () -> new DefaultSignalListener<>() {
            @Override
            public void doOnComplete() {
                runnable.run();
            }
        };
    }
}
