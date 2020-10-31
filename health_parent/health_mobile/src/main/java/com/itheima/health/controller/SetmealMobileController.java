package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.Utils.QiNiuUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: SetmealMobileController$
 * @Author Admin
 * @Date: 2020-10-28$ 17:35$
 * @Version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    private SetmealService setmealService;
    /** 功能描述:
    查询所有套餐：页面显示 图片+详情
    * @return:
    * @Author: admin
    * @Date: 2020-10-28 17:37
    */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> list = setmealService.findAll();
        //图片
        list.forEach(s -> s.setImg(QiNiuUtils.DOMAIN+s.getImg()));
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    /** 功能描述:
    查询套餐详情
    * @return:
    * @Author: admin
    * @Date: 2020-10-28 20:39
    */

    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
       Setmeal setmeal =  setmealService.findDetailById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }


    @GetMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
