package com.shard.springbootshardingjdbc.readwrite.algorithm;

import com.shard.springbootshardingjdbc.readwrite.utils.HashUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Getter
public class TableLocator extends AbstractLocator {

    //虚拟节点最小为1，为1时等于实际节点
    @Setter
    private int virtualNodeCount = 1;

    public void init(String logicalTableName, Collection<String> originTableNodes, Collection<String> bindingTables) {
        super.logicalTableName = logicalTableName;
        if (!CollectionUtils.isEmpty(bindingTables)) {
            ArrayList<String> bindingTablesCopy = new ArrayList<>(bindingTables);
            bindingTablesCopy.remove(logicalTableName);
            Optional<ConsistentHashAlgorithm> existedLocator = bindingTablesCopy.stream()
                    .map(LocatorHolder::get)
                    .filter(Objects::nonNull)
                    .findFirst();
            if (existedLocator.isPresent()) {
                ConsistentHashAlgorithm locator = existedLocator.get();
                SortedMap<Long, String> virtualNodeMap = locator.getVirtualNodeMap();
                SortedMap<Long, String> nodeMap = new TreeMap<>();
                virtualNodeMap.forEach((k, v) -> nodeMap.put(k, v.replace(locator.getLogicalTableName(), logicalTableName)));
                virtualNodes = nodeMap;
                return;
            }
        }
        super.init(originTableNodes, virtualNodeCount);
    }

    /**
     * 通过计算key的hash
     * 计算映射的节点
     */
    @Nullable
    @Override
    public String findRealNode(String key) {
        if (ObjectUtils.isEmpty(key)) {
            return null;
        }
        String virtualNode = findVirtualNode(key);
        //虚拟节点名称截取后获取真实节点
        if (!ObjectUtils.isEmpty(virtualNode)) {
            return virtualNode.substring(0, virtualNode.indexOf("-"));
        }
        return null;
    }

    /**
     * 获取虚拟节点
     */
    @Nullable
    @Override
    public String findVirtualNode(String key) {
        long hash = HashUtil.getHash(key);
        // 得到大于该Hash值的所有Map
        SortedMap<Long, String> subMap = virtualNodes.tailMap(hash);
        if (!subMap.isEmpty()) {
            //第一个Key就是顺时针过去离node最近的那个结点
            Long i = subMap.firstKey();
            //返回对应的服务器
            return subMap.get(i);
        }
        //取virtualNodes第一个节点
        Long i = virtualNodes.firstKey();
        return virtualNodes.get(i);
    }

    public static TableLocator getInstance() {
        return new TableLocator();
    }

}
