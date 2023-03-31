package com.shard.springbootshardingjdbc.readwrite.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.shard.shardingjdbc.utils.R;

@FeignClient(name = "storage-service",path="/storage")
public interface StorageApi {
    @GetMapping("/reduce")
    R<Boolean> reduce(@RequestParam("id") String id, @RequestParam("count") int count);
}
