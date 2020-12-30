package com.hn.rbac.server.cache.function;


import com.hn.rbac.server.cache.exception.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R execute(T t) throws RedisConnectException;
}
