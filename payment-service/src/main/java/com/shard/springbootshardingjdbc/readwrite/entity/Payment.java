package com.shard.springbootshardingjdbc.readwrite.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Payment {
    private String id;
    private String message;
    private String status;
    private String createTime;
    List<PaymentDetail> details;
}
