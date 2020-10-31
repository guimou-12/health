package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    //采用+1的方式，更方便通过日志操作，后面and number>reservations添加锁，防止并发问题
    List<Order> findByCondition(Order order);

    void add(Order order);

    Map<String, Object> findById(int id);
}
