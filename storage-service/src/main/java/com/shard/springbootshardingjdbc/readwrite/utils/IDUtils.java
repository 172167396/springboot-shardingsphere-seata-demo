package com.shard.springbootshardingjdbc.readwrite.utils;

import java.util.UUID;

public class IDUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        int count = 0;
        for (int i = 0;; ++count) {
            System.out.println(count);
            if(count == 10000) break;
            System.out.println("已结束");
        }
    }
}
