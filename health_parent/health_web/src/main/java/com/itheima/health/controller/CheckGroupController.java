package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

   @GetMapping("/findAll")
    public Result findAll(){
       List<CheckGroup> list =  checkGroupService.findAll();
       return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }

    /** 功能描述:
    添加检查组组
    * @return:
    * @Author: admin
    * @Date: 2020-10-24 18:15
    */
   @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
       checkGroupService.add(checkGroup,checkitemIds);
       return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);

    }

    @PostMapping("/findPage")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean){
       PageResult<CheckGroup> pageResult =  checkGroupService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);

    }

    /** 功能描述:
    通过id获取检查组
    * @return:
    * @Author: admin
    * @Date: 2020-10-25 0:11
    */
    @GetMapping("/findById")
    public Result findById(int id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    /** 功能描述:
    通过检查组id查找检查项id
    * @return:
    * @Author: admin
    * @Date: 2020-10-25 0:16
    */
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int id){
        List<Integer> checkitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkitemIds);
    }

    
    /** 功能描述:
    提交修改
    * @return: 
    * @Author: admin
    * @Date: 2020-10-25 0:19
    */
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){

        checkGroupService.update(checkGroup, checkitemIds);

        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /** 功能描述:
    删除检查组
    * @return:
    * @Author: admin
    * @Date: 2020-10-25 0:31
    */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        checkGroupService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
