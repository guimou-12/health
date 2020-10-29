package com.itheima.health.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Description: GenerateHtmlJob$
 * @Author Admin
 * @Date: 2020-10-28$ 23:11$
 * @Version 1.0
 */
@Component("GenerateHtmlJob")
public class GenerateHtmlJob {
    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);

    @Autowired
    private JedisPool jedisPool;

    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void generateHtml(){
        // 获取要生成的套餐的id
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        // 需要处理的套餐id集合
        Set<String> ids = jedis.smembers(key);
        // id|操作符|时间戳
        log.debug("ids: {}", ids==null?0:ids.size());
        if(null != ids && ids.size() > 0){
            for (String id : ids) {
                // id|操作符|时间戳
                log.debug("id: {}", id);
                String[] split = id.split("\\|");
                String setmealId = split[0];// 套餐id
                String operator = split[1];
                if("1".equals(operator)){
                    // 生成套餐详情静态页面
                    log.info("生成套餐详情静态页面");
                }else if("0".equals(operator)){
                    log.info("删除静态页面");
                }
            }
            //生成静态的套餐列表页面
            log.info("生成静态的套餐列表页面");
        }
    }

}
