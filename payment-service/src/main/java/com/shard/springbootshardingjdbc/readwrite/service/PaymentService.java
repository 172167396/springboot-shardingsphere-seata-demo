package com.shard.springbootshardingjdbc.readwrite.service;

import com.shard.springbootshardingjdbc.readwrite.entity.Payment;
import com.shard.springbootshardingjdbc.readwrite.entity.PaymentAndDetail;

import java.util.List;

public interface PaymentService {

    void savePayment(Payment payment);

    List<Payment> getPayment(String[] ids);

    int count();

    List<PaymentAndDetail> getPaymentAndDetail(String id);

    List<PaymentAndDetail> getPaymentAndDetailNoWhere();

}
