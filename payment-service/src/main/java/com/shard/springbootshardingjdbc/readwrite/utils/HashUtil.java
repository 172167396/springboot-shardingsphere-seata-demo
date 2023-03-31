package com.shard.springbootshardingjdbc.readwrite.utils;

public class HashUtil {

    /**
     * 使用FNV1_32_HASH算法计算key的Hash值
     */
    public static long getHash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++)
            hash = (hash ^ key.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }
}
