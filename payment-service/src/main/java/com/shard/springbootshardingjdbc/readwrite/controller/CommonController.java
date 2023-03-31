package com.shard.springbootshardingjdbc.readwrite.controller;

import com.shard.springbootshardingjdbc.readwrite.entity.Payment;
import com.shard.springbootshardingjdbc.readwrite.entity.PaymentAndDetail;
import com.shard.springbootshardingjdbc.readwrite.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
public class CommonController {

    @Resource
    PaymentService paymentService;


    @PostMapping("/savePayment")
    public String savePayment(@RequestBody Payment payment) {
        paymentService.savePayment(payment);
        return "success";
    }

    @GetMapping("/getPayment")
    public List<Payment> getPayment(String[] ids) {
        return paymentService.getPayment(ids);
    }


    @GetMapping("/getPaymentAndDetail")
    public List<PaymentAndDetail> getPaymentAndDetail(String id) {
        return paymentService.getPaymentAndDetail(id);
    }

    @GetMapping("/getPaymentAndDetailNoWhere")
    public List<PaymentAndDetail> getPaymentAndDetailNoWhere() {
        return paymentService.getPaymentAndDetailNoWhere();
    }

    @GetMapping("/count")
    public int count() {
        return paymentService.count();
    }
}
