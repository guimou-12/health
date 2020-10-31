package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.Utils.SMSUtils;
import com.itheima.health.Utils.ValidateCodeUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: ValidateCodeController$
 * @Author Admin
 * @Date: 2020-10-29$ 17:34$
 * @Version 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    /** 功能描述:
    接收手机号码，从redis取值
     有：发送过了
     没有：生成验证码 重新发送，存入redis加入有效时间，过期失效
    * @return: com.itheima.health.entity.Result
    * @Author: admin
    * @Date: 2020-10-29 17:38
    */
    @Autowired
    private JedisPool jedisPool;


    @PostMapping("/send4Order")
    public Result send4Order(String telephone){
        //从redis中检查是否发送过
        Jedis jedis = jedisPool.getResource();
        //验证码类型+手机号码
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        //对redis取验证码判断
        String codeInRedis = jedis.get(key);
        if(null == codeInRedis){
            //生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                //发送
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                //存入redis加入有效时间，过期失效
                jedis.setex(key,10*60,code+"");
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);

            }
        }
        return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);

    }
}
