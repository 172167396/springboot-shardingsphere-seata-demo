package com.shard.springbootshardingjdbc.readwrite.service.impl;

import com.shard.springbootshardingjdbc.readwrite.api.StorageApi;
import com.shard.springbootshardingjdbc.readwrite.dao.PaymentMapper;
import com.shard.springbootshardingjdbc.readwrite.entity.Payment;
import com.shard.springbootshardingjdbc.readwrite.entity.PaymentAndDetail;
import com.shard.springbootshardingjdbc.readwrite.entity.PaymentDetail;
import com.shard.springbootshardingjdbc.readwrite.service.PaymentService;
import com.shard.springbootshardingjdbc.readwrite.utils.IDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    PaymentMapper paymentMapper;

    @Resource
    StorageApi storageApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ShardingSphereTransactionType(value = TransactionType.BASE)
    public void savePayment(Payment payment) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String paymentId = !StringUtils.hasLength(payment.getId()) ? IDUtils.uuid() : payment.getId();
        payment.setId(paymentId);
        paymentMapper.savePayment(payment);
        stopWatch.stop();
        log.info("save payment cost {}ms", stopWatch.getLastTaskTimeMillis());
        List<PaymentDetail> details = payment.getDetails();
        if (!CollectionUtils.isEmpty(details)) {
            stopWatch.start();
            details.forEach(detail -> {
                detail.setId(IDUtils.uuid());
                detail.setPaymentId(paymentId);
                storageApi.reduce(detail.getStoreId(), detail.getCount()).requireApiData();
            });
            stopWatch.stop();
            log.info("feign api cost {}ms", stopWatch.getLastTaskTimeMillis());
            stopWatch.start();
            paymentMapper.savePaymentDetails(details);
            stopWatch.stop();
            log.info("save detail cost {}ms", stopWatch.getLastTaskTimeMillis());
        }
//        if(1== 1) throw new RuntimeException("模拟报错");
    }

    @Override
    public List<Payment> getPayment(String[] ids) {
        List<String> idList = Arrays.asList(ids);
//        HintManager.getInstance().setMasterRouteOnly();
        return paymentMapper.getPayment(idList);
    }

    @Override
    public int count() {
        return paymentMapper.count();
    }

    @Override
    public List<PaymentAndDetail> getPaymentAndDetail(String id) {
        return paymentMapper.getPaymentAndDetail(id);
    }

    @Override
    public List<PaymentAndDetail> getPaymentAndDetailNoWhere() {
        return paymentMapper.getPaymentAndDetailNoWhere();
    }

}
