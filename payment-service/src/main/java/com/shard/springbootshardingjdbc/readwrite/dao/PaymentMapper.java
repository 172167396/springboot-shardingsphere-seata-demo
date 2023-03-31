package com.shard.springbootshardingjdbc.readwrite.dao;

import com.shard.springbootshardingjdbc.readwrite.entity.Payment;

import com.shard.springbootshardingjdbc.readwrite.entity.PaymentAndDetail;
import com.shard.springbootshardingjdbc.readwrite.entity.PaymentDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PaymentMapper {

    @Insert("insert into tb_pay (id,message,status,create_time) values(#{id},#{message},#{status},#{createTime})")
    void savePayment(Payment payment);

    @Select("<script>select id,message,status,create_time from tb_pay where id in <foreach collection=\"ids\" item=\"id\" separator=\",\" open=\"(\" close=\")\">#{id}</foreach></script>")
    List<Payment> getPayment(@Param("ids") Collection<String> ids);

    @Select("select count(1) from tb_pay")
    int count();

    @Insert("insert into tb_pay_detail(id,payment_id,storage_id,money,count,create_time) values (#{id},#{paymentId},#{money},#{count},#{createTime})")
    void savePaymentDetail(PaymentDetail details);

    @Insert("<script>insert into tb_pay_detail(id,payment_id,storage_id,money,count,create_time) values <foreach collection=\"details\" item=\"detail\" separator=\",\">(#{detail.id},#{detail.paymentId},#{detail.storeId},#{detail.money},#{detail.count},#{detail.createTime})</foreach></script>")
    void savePaymentDetails(@Param("details") List<PaymentDetail> details);

    @Select("SELECT p.id,p.message,p.status,d.id detailId,d.storage_id,d.money,d.count,d.create_time FROM tb_pay p LEFT JOIN tb_pay_detail d on p.id = d.payment_id where p.id = #{value}")
    List<PaymentAndDetail> getPaymentAndDetail(String id);

    @Select("SELECT p.id,p.message,p.status,d.id detailId,d.money,d.count,d.create_time FROM tb_pay p LEFT JOIN tb_pay_detail d on p.id = d.payment_id")
    List<PaymentAndDetail> getPaymentAndDetailNoWhere();
}
