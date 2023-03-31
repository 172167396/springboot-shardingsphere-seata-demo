package com.shard.springbootshardingjdbc.readwrite.algorithm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocatorHolder {
    private static final Map<String, ConsistentHashAlgorithm> container = new ConcurrentHashMap<>();

    public static void put(String key, ConsistentHashAlgorithm locator) {
        container.putIfAbsent(key, locator);
    }

    public static ConsistentHashAlgorithm get(String key) {
        return container.get(key);
    }
}
