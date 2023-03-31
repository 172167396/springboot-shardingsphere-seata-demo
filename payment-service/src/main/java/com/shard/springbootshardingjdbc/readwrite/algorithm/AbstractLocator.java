package com.shard.springbootshardingjdbc.readwrite.algorithm;

import com.shard.springbootshardingjdbc.readwrite.utils.HashUtil;
import lombok.Getter;

import java.util.*;

@Getter
public abstract class AbstractLocator implements ConsistentHashAlgorithm {

    protected String logicalTableName;
    protected SortedMap<Long, String> virtualNodes;

    protected volatile boolean initFinished;


    @Override
    public SortedMap<Long, String> getVirtualNodeMap() {
        return virtualNodes;
    }

    protected void init(Collection<String> originNodes, int virtualNodeCount) {
        if (!initFinished) {
            synchronized (this) {
                if (!initFinished) {
                    SortedMap<Long, String> virtualTableNodes = new TreeMap<>();
                    for (String node : originNodes) {
                        for (int i = 0; i < virtualNodeCount; i++) {
                            String virtualNodeName = node + "-VN" + i;
                            long hash = HashUtil.getHash(virtualNodeName);
                            virtualTableNodes.put(hash, virtualNodeName);
                        }
                    }
                    this.virtualNodes = virtualTableNodes;
                    this.initFinished = true;
                }
            }
        }
    }

    @Override
    public String getLogicalTableName() {
        return logicalTableName;
    }
}
