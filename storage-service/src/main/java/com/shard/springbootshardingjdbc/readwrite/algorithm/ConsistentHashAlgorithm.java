package com.shard.springbootshardingjdbc.readwrite.algorithm;


import java.util.SortedMap;

public interface ConsistentHashAlgorithm {

    String findRealNode(String key);

    String findVirtualNode(String key);

    SortedMap<Long,String> getVirtualNodeMap();

    String getLogicalTableName();
}
