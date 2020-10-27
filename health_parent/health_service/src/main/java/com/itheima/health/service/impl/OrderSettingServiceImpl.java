package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: OrderSettingServiceImpl$
 * @Author Admin
 * @Date: 2020-10-26$ 19:15$
 * @Version 1.0
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    /** 功能描述:
    批量导入
     * --遍历数据

     --通过日期查询预约信息
     --有，则判断预约总数与对比已预约数，小于报异常
     --大于：更新已预约数
     --无则添加

    * @return: void
    * @Author: admin
    * @Date: 2020-10-26 19:55
    */
    @Transactional
    @Override
    public void add(List<OrderSetting> list) {
        for (OrderSetting orderSetting : list) {
           OrderSetting osInDB =  orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
           if(osInDB != null){
               if(osInDB.getNumber()<osInDB.getReservations()){
                   throw new HealthException("最大预约数不能小于已已预约数");
               }
               orderSettingDao.updateNumber(orderSetting);
            }else {
               orderSettingDao.add(orderSetting);
           }
        }


    }

    /** 功能描述:
     根据时间查询预约信息
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Integer>>
    * @Author: admin
    * @Date: 2020-10-27 12:11
    */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        month += "%";
        List<Map<String, Integer>> list = orderSettingDao.getOrderSettingByMonth(month);
        return list;
    }

    /** 功能描述:
    设置预约信息
     ->判断是否存在预约设置
       ->有：进行预约总人数与已预约人数的判断
            ->少则报异常
            ->多则更新
      ->无：添加预约信息
    * @return: void
    * @Author: admin
    * @Date: 2020-10-27 12:38
    */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) throws HealthException {
        OrderSetting os = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if(null!= os){
            if(os.getNumber()<os.getReservations()){
                throw new HealthException("最大预约人数不能小已预约人数！");
            }else {
                orderSettingDao.updateNumber(orderSetting);
            }
        }
        orderSettingDao.add(os);
    }
}
