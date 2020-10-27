package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.Utils.POIUtils;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: OrderSettingController
 * @Author Admin
 * @Date: 2020-10-26 18:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    private static final Logger log = LoggerFactory.getLogger(OrderSettingController.class);

    @Reference
    private OrderSettingService orderSettingService;

    /** 功能描述:
    批量导入设置信息
     --读取表格数据
     --创建对象集合
     --将数据封装到对象
     --执行导入操作
     * @return: com.itheima.health.entity.Result
     * @Author: admin
     * @Date: 2020-10-26 18:22
     */

    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile)  throws Exception{
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> list = new ArrayList<>();
            //日期格式转换
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            OrderSetting os = null;
            for (String[] string : strings) {
                Date date = sdf.parse(string[0]);
                os = new OrderSetting(date,Integer.valueOf(string[1]));
                list.add(os);
            }
            orderSettingService.add(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (Exception e) {
            log.error("导入预约设置失败",e);
            throw e;
        }
    }

    /** 功能描述:
    根据时间查询预约信息
    * @return: com.itheima.health.entity.Result
    * @Author: admin
    * @Date: 2020-10-27 12:09
    */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
       List<Map<String,Integer>> list = orderSettingService.getOrderSettingByMonth(month);
       return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);

    }

    /** 功能描述:

     预约设置
    * @return: com.itheima.health.entity.Result
    * @Author: admin
    * @Date: 2020-10-27 12:34
    */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
         orderSettingService.editNumberByDate(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);

    }


}
