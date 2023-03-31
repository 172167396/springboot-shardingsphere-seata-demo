package com.shard.springbootshardingjdbc.readwrite.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentAndDetail {
    private String id;
    private String message;
    private String status;
    private String detailId;
    private String storageId;
    private String money;
    private String count;
    private LocalDateTime createTime;
}
