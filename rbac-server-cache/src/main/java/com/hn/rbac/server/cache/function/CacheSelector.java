package com.hn.rbac.server.cache.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
