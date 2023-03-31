package com.shard.springbootshardingjdbc.readwrite.service.impl;

import com.shard.springbootshardingjdbc.readwrite.dao.StorageMapper;
import com.shard.springbootshardingjdbc.readwrite.entity.Storage;
import com.shard.shardingjdbc.exception.AppRuntimeException;
import com.shard.springbootshardingjdbc.readwrite.service.StorageService;
import com.shard.springbootshardingjdbc.readwrite.utils.IDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    StorageMapper storageMapper;

    @Override
    public void saveStorage(Storage storage) {
        String paymentId = !StringUtils.hasLength(storage.getId()) ? IDUtils.uuid() : storage.getId();
        storage.setId(paymentId);
        storageMapper.saveStorage(storage);
    }

    @Override
    public List<Storage> getStorage(String[] ids) {
        List<String> idList = Arrays.asList(ids);
//        HintManager.getInstance().setMasterRouteOnly();
        return storageMapper.getStorage(idList);
    }

    @Override
    public int count() {
        return storageMapper.count();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ShardingSphereTransactionType(value = TransactionType.BASE)
    public void reduceCount(String id, int count) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        if (count <= 0) {
            throw new AppRuntimeException("库存减少数量不能小于0");
        }
        Storage storage = storageMapper.getStorageById(id);
        if (storage == null) {
            throw new AppRuntimeException("库存商品不存在");
        }
        int afterCount = storage.getCount() - count;
        if (afterCount < 0) {
            throw new AppRuntimeException("库存不足");
        }
        //不考虑并发问题
        storageMapper.reduceCount(id, afterCount);
        stopWatch.stop();
        log.info("update storage cost {}ms", stopWatch.getLastTaskTimeMillis());
    }

}
