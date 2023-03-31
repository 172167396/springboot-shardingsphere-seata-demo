# springboot-shardingsphere-jdbc-demo
### 简介
这是一个整合springboot + shardingsphere-jdbc + seata的demo项目，使用nacos作为配置和注册中心，在shardingjdbc基础上添加了分布式事务 <br>
仅供参考


### 准备
1. nacos
 使用的是docker版的nacos/nacos-server:latest

2. seata<br>
 使用的是docker版的seataio/seata-server:latest，具体多少版本不清楚
 新版的seata只有一个application.yml，照着修改即可<br>
 到nacos里新建namespace为seata，然后导入env/seata/nacosconfig/seata_nacos_config.zip
 **如果使用的是db模式，则需要在mysql里执行seata.sql文件**<br>
 以下config请自行修改：service.default.grouplist、store.db.url、store.db.user、store.db.password
3. 数据库
 此demo使用的是docker的mysql8.0.29版本，使用了两个主库两个从库<br>
 分别在两个主库新建db_payment0、db_payment1数据库，并执行env/db下的sql<br>
 主从复制不做过多介绍，保证可用即可<br>
4. 项目配置
 在nacos上新建src/main/filters/config-dev.properties里指定的namespace，然后导入
env/ymlexample下的配置并做对应修改<br>
 并修改子项目resources目录下的registry.conf里的nacos相关配置，**seata.conf中的transaction.service.group必须和seata namespace中的service.vgroupMapping.xxx一致**<br>
 
### 使用
 项目内写了几个保存和查询方法，用来测试分库和分表，其中tb_pay和tb_pay_detail做了绑定表<br>
 savePayment方法应用了分布式事务，可修改代码手动抛出异常<br>
 分布式事务生效的条件为方法同时加@Transactional和@ShardingSphereTransactionType(value = TransactionType.BASE)<br>
 加@ShardingSphereTransactionType的作用就是改变当前事务的类型为BASE,transactionManager应用为SeataATShardingSphereTransactionManager
### 结语
 此demo的改法参考了issue#22356 @huangxiuqi推荐的代码<br>
 在整合过程中遇到过许许多多的问题，比如一开始使用的是<br>
 sharding-jdbc-spring-boot-starter:4.1.1
 开始我以为是版本不对，改用shardingsphere的依赖<br>
 yml配置和shardingsphere-jdbc-core-spring-boot-starter:5.2.0的大不相同<br>


申明，由于硬件或软件配置原因，以下结果因人而异<br>
经过多次接口调用测试，带与不带分布式事务的方法执行耗时相差比较大，以savePayment为例，样例json
```json
{
    "id": "",
    "message": "矿卡大采购",
    "status": "0",
    "createTime": "2023-03-30 13:07:32",
    "details": [
        {
            "id": "",
            "paymentId": "",
            "storeId": "654a935f7cf0416f8a22fc4aca2909c2",
            "money": "1579",
            "count": "2",
            "createTime": "2023-03-30 13:07:32"
        },
        {
            "id": "",
            "paymentId": "",
            "storeId": "ac12bd7552b142ab920cf5335601463a",
            "money": "1899",
            "count": "2",
            "createTime": "2023-03-30 13:07:32"
        }
    ]
}
```
storage提前整几条数据进去，然后整个方法耗时平均在2秒+，不加分布式事务整体耗时平均400ms+<br>
慢可能是种种原因导致的，也可能是待优化项<br>

demo的不足之处请谅解，欢迎交流沟通!
