package com.shard.springbootshardingjdbc.readwrite.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IdGenerator {
    /**
     * 1号机器  0-31
     */
    @Value("${customize.node-id:1}")
    private long nodeId = 1;

    private Snowflake snowflake;

    @PostConstruct
    public void init() {
        long workerId;
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr()) % 32;
        } catch (Exception e) {
            e.printStackTrace();
            workerId = NetUtil.getLocalhostStr().hashCode() % 32;
        }
        snowflake = IdUtil.getSnowflake(workerId, nodeId);
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public Number generateKey() {
        return snowflakeId();
    }
}