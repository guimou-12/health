package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    void updateNumber(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    OrderSetting findByOrderDate(Date orderDate);

    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    int editReservationsByOrderDate(OrderSetting orderSetting);

}
