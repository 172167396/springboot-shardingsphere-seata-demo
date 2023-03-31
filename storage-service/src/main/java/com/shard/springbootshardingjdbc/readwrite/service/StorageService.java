package com.shard.springbootshardingjdbc.readwrite.service;

import com.shard.springbootshardingjdbc.readwrite.entity.Storage;

import java.util.List;

public interface StorageService {

    void saveStorage(Storage storage);

    List<Storage> getStorage(String[] ids);

    int count();


    void reduceCount(String id, int count);

}
