package com.shard.springbootshardingjdbc.readwrite.algorithm;

public interface NodeLocatorSelector {

    ConsistentHashAlgorithm guessLocatorType(String nodeName);
}
