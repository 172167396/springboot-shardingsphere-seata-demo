package com.shard.springbootshardingjdbc.readwrite.controller;

import com.shard.springbootshardingjdbc.readwrite.entity.Storage;
import com.shard.springbootshardingjdbc.readwrite.service.StorageService;
import org.springframework.web.bind.annotation.*;
import com.shard.shardingjdbc.utils.R;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/storage")
@RestController
public class CommonController {

    @Resource
    StorageService storageService;


    @PostMapping("/saveStorage")
    public String savePayment(@RequestBody Storage storage) {
        storageService.saveStorage(storage);
        return "success";
    }

    @GetMapping("/getStorage")
    public List<Storage> getStorage(String[] ids) {
        return storageService.getStorage(ids);
    }

    @GetMapping("/reduce")
    public R<Boolean> reduce(@RequestParam String id, @RequestParam int count) {
        storageService.reduceCount(id,count);
        return R.success();
    }

    @GetMapping("/count")
    public int count() {
        return storageService.count();
    }
}
