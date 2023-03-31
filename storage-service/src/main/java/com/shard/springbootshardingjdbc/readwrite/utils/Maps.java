package com.shard.springbootshardingjdbc.readwrite.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Maps<K, V> {

    private Map<K, V> container;


    public static <K, V> Maps<K, V> newHashMap() {
        Maps<K, V> maps = new Maps<>();
        maps.container = new HashMap<>();
        return maps;
    }

    public static <K, V> Maps<K, V> of(K k, V v) {
        Maps<K, V> maps = new Maps<>();
        maps.container = new HashMap<>();
        maps.put(k, v);
        return maps;
    }

    public V get(K key) {
        return container.get(key);
    }

    public V put(K k, V v) {
        return container.put(k, v);
    }
}
