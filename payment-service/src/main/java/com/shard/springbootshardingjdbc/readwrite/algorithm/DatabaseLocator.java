package com.shard.springbootshardingjdbc.readwrite.algorithm;


import com.shard.springbootshardingjdbc.readwrite.utils.HashUtil;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.SortedMap;


@Getter
public class DatabaseLocator extends AbstractLocator {

    private static DatabaseLocator instance;

    private static final int virtualNodeCount = 1;

    private DatabaseLocator() {
        super();
    }

    public void init(Collection<String> originNodes) {
        init(originNodes, virtualNodeCount);
    }


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

    public static synchronized DatabaseLocator getInstance() {
        if (instance == null) {
            instance = new DatabaseLocator();
        }
        return instance;
    }
}
