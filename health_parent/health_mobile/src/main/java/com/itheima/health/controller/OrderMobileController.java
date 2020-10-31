package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Description: OrderMobileController$
 * @Author Admin
 * @Date: 2020-10-29$ 18:11$
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 功能描述:
     * 提交预约
     * --验证码校验
     * --设置预约类型
     * --预约成功页面展示是需要的id
     *
     * @return:
     * @Author: admin
     * @Date: 2020-10-29 18:12
     */

    @PostMapping("/submit")
    public Result sumbit(@RequestBody Map<String, String> orderInfo) {
        Jedis jedis = jedisPool.getResource();
        String telephone = orderInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;

        String codeInRedis = jedis.get(key);
        if (null == codeInRedis) {
            //没值
            return new Result(false, "请重新获取验证码");
        }
        //前端验证码
        String validateCode = orderInfo.get("validateCode");
        if (!codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //移除验证码，防止重复
        jedis.del(key);
        //设置预约类型
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        Order order = orderService.submit(orderInfo);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS, order);
    }

    @GetMapping("/findById")
    public Result findById(int id){
       Map<String ,Object> orderInfo = orderService.findById(id);
       return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}


