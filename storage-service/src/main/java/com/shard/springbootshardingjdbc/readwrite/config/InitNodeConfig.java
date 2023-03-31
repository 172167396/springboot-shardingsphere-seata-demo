package com.shard.springbootshardingjdbc.readwrite.config;


import com.shard.shardingjdbc.exception.AppRuntimeException;
import com.shard.springbootshardingjdbc.readwrite.algorithm.DatabaseLocator;
import com.shard.springbootshardingjdbc.readwrite.algorithm.LocatorHolder;
import com.shard.springbootshardingjdbc.readwrite.algorithm.TableLocator;
import com.shard.springbootshardingjdbc.readwrite.utils.Maps;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.datanode.DataNode;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.rule.BindingTableRule;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sharding.rule.TableRule;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class InitNodeConfig {

    @Resource
    DataSource dataSource;

    @PostConstruct
    public void init() {
        Class<? extends ShardingSphereDataSource> clazz = ((ShardingSphereDataSource) dataSource).getClass();
        ContextManager contextManager;
        try {
            Field contextManagerField = clazz.getDeclaredField("contextManager");
            contextManagerField.setAccessible(true);
            contextManager = (ContextManager)contextManagerField.get(dataSource);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AppRuntimeException(e.getMessage());
        }

        ArrayList<ShardingSphereRule> logicDb = new ArrayList<>(contextManager.getMetaDataContexts().getMetaData().getDatabase("logic_db").getRuleMetaData().getRules());

        ShardingRule rule = (ShardingRule) logicDb.get(1);
        Collection<TableRule> tableRules = rule.getTableRules().values();
        Collection<BindingTableRule> bindingTableRules = rule.getBindingTableRules().values();
        Maps<String, Collection<String>> bindingMap = Maps.newHashMap();
        bindingTableRules.forEach(bindingTableRule -> {
            List<String> bindingTables = bindingTableRule.getTableRules().values().stream().map(TableRule::getLogicTable).collect(Collectors.toList());
            bindingTables.forEach(tb -> bindingMap.put(tb, bindingTables));
        });
        tableRules.forEach(tableRule -> {
            String logicTable = tableRule.getLogicTable();
            Collection<String> tableNames = tableRule.getActualDataNodes().stream()
                    .map(DataNode::getTableName)
                    .collect(Collectors.toSet());
            TableLocator tableLocator = TableLocator.getInstance();
            tableLocator.init(logicTable, tableNames, bindingMap.get(logicTable));
            LocatorHolder.put(logicTable, tableLocator);
        });

        Collection<String> dataSourceNames = rule.getDataSourceNames();
        if (dataSourceNames != null && !dataSourceNames.isEmpty()) {
            DatabaseLocator databaseLocator = DatabaseLocator.getInstance();
            databaseLocator.init(dataSourceNames);
            LocatorHolder.put("ds", databaseLocator);
        }
    }
}
