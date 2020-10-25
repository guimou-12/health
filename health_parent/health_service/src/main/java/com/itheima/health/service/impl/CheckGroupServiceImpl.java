package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Transactional
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
       //添加检查组
        checkGroupDao.add(checkGroup);

        //获取检查组id
        Integer checkGroupId = checkGroup.getId();
        //添加检查项
        if (null != checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId,checkitemId);
            }
        }
    }

    /** 功能描述:
    分页条件查询
    * @return: com.itheima.health.entity.PageResult<com.itheima.health.pojo.CheckGroup>
    * @Author: admin
    * @Date: 2020-10-24 20:19
    */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        //调用pageHelp
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        //按条件拼接
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }

        //分页
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());

        return new PageResult<CheckGroup>(page.getTotal(), page.getResult());


    }
    /** 功能描述:
    通过检查组id获取检查组
    * @return: com.itheima.health.pojo.CheckGroup
    * @Author: admin
    * @Date: 2020-10-25 0:18
    */
    @Override
    public CheckGroup findById(int checkGroupId) {
        return checkGroupDao.findById(checkGroupId);
    }

    /** 功能描述:
    通过检查组id查找检查项id
    * @return: com.itheima.health.pojo.CheckGroup
    * @Author: admin
    * @Date: 2020-10-25 0:19
    */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //添加事务
    @Transactional
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
    //更新检查组
        checkGroupDao.update(checkGroup);
    // 删除旧关系(根据检查组id)
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
    //添加新关系
        if(null !=checkitemIds ){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(),checkitemId);
            }
        }
    }
    @Transactional
    @Override
    public void deleteById(int id) {
        //通过检查组id查询是否被套餐使用
        int count = checkGroupDao.findSetmealCountByCheckGroupId(id);
        if(count>0){
            throw  new HealthException(MessageConstant.CHECKGROUP_IN_USE);
        }
        //删除检查组与检查项的关系
        checkGroupDao.deleteCheckGroupCheckItem(id);
        //删除检查组
        checkGroupDao.deleteById(id);
    }

}
