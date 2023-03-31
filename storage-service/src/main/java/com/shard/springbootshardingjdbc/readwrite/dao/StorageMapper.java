package com.shard.springbootshardingjdbc.readwrite.dao;

import com.shard.springbootshardingjdbc.readwrite.entity.Storage;

import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

@Mapper
public interface StorageMapper {

    @Insert("insert into tb_storage (id,count,update_time) values(#{id},#{count},now())")
    void saveStorage(Storage storage);

    @Select("select id,count,update_time from tb_storage where id = #{value}")
    Storage getStorageById(String id);

    @Select("<script>select id,count,update_time from tb_storage where id in <foreach collection=\"ids\" item=\"id\" separator=\",\" open=\"(\" close=\")\">#{id}</foreach></script>")
    List<Storage> getStorage(@Param("ids") Collection<String> ids);

    @Select("select count(1) from tb_storage")
    int count();

    @Update("update tb_storage set count = #{count} where id = #{id}")
    void reduceCount(@Param("id") String id, @Param("count") int count);

}
