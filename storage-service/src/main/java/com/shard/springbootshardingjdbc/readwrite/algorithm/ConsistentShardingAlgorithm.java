package com.shard.springbootshardingjdbc.readwrite.algorithm;

import com.shard.shardingjdbc.exception.SharingIllegalArgsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

@Slf4j
@SuppressWarnings("unused")
public class ConsistentShardingAlgorithm
        implements StandardShardingAlgorithm<String>, NodeLocatorSelector {

    /**
     * 精确分片
     * 一致性hash算法
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        if (CollectionUtils.isEmpty(availableTargetNames)) {
            return shardingValue.getLogicTableName();
        }
        ArrayList<String> availableTargetNameList = new ArrayList<>(availableTargetNames);
        String logicalName = availableTargetNameList.get(0).replaceAll("[^(a-zA-Z_)]", "");
        ConsistentHashAlgorithm consistentHashAlgorithm = guessLocatorType(logicalName);
        return consistentHashAlgorithm.findRealNode(String.valueOf(shardingValue.getValue()));
    }

    /**
     * 范围查询规则
     * 可以根据实际场景进行修改
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding results for data sources or tables's names
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> shardingValue) {
        return availableTargetNames;
    }

    /**
     * @param nodeName 节点名称
     * @return 数据库或表的查找类
     */
    @Override
    public ConsistentHashAlgorithm guessLocatorType(String nodeName) {
        if (ObjectUtils.isEmpty(nodeName)) {
            throw new SharingIllegalArgsException("节点名称不能为空");
        }
        if (nodeName.endsWith("_")) {
            nodeName = nodeName.substring(0, nodeName.length() - 1);
        }
        return LocatorHolder.get(nodeName);
    }



    @Override
    public String getType() {
        return "ConsistentHash";
    }

    @Override
    public Properties getProps() {
        return new Properties();
    }

    @Override
    public void init(Properties properties) {
        log.info(properties.toString());
    }
}
