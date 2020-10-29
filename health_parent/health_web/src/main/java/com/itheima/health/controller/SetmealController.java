package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.Utils.QiNiuUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//返回json数据不需要在参数前加@requestbody
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;

    /** 功能描述:
    上传图片
    * @return: com.itheima.health.entity.Result
    * @Author: admin
    * @Date: 2020-10-25 19:28
    */
    @RequestMapping("/upload")
    //MultipartFile类：接受前台传过来的文件
    //参数名必须与前端传的name参数名一致
    public Result upload(MultipartFile imgFile){
        //三高.jpg
        // 获取原有图片后
        String originalFilename = imgFile.getOriginalFilename();
        // 取缀名  .jpg
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成唯一文件名（存在上传相同图片名字相同），拼接后缀名
        String uniqueName = UUID.randomUUID().toString()+ extension;
        //调用七牛云工具类上传文件
        try {
            //
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),uniqueName);
            //返回数据
            HashMap<String, String> map = new HashMap<>();
            map.put("domain",QiNiuUtils.DOMAIN);
            map.put("imgName",uniqueName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务添加
        Integer setmealId = setmealService.add(setmeal,checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        jedis.sadd(key,setmealId+"|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }


    @PostMapping("/findPage")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    @GetMapping("/findById")
    public Result findById(int id){
        //通过id查询套餐信息
        Setmeal setmeal =  setmealService.findById(id);
        //响应数据给前端回显图片
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("setmeal",setmeal);
        resultMap.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    /** 功能描述:
    通过套餐id查询所有检查组ids
    * @return: com.itheima.health.entity.Result
    * @Author: admin
    * @Date: 2020-10-26 0:01
    */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){

        List<Integer> checkgroupIds =  setmealService.findCheckgroupIdsBySetmealId(id);

        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
    }

    /** 功能描述:
    修改
    * @return:
    * @Author: admin
    * @Date: 2020-10-26 0:06
    */

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务修改
        setmealService.update(setmeal,checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setmeal:static:html",setmeal.getId() + "|1|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    /** 功能描述:
     修改
     * @return:
     * @Author: admin
     * @Date: 2020-10-26 0:06
     */

    @PostMapping("/deleteById")
    public Result deleteById(int id){
        setmealService.deleteById(id);
        Jedis jedis = jedisPool.getResource();
        // 操作符0代表删除
        jedis.sadd("setmeal:static:html",id + "|0|" + System.currentTimeMillis());
        jedis.close();
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
