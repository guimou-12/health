package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /** 功能描述:
    添加套餐
    * @return: void
    * @Author: admin
    * @Date: 2020-10-25 23:06
    */
    @Transactional
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐
        setmealDao.add(setmeal);
        //获取套餐ID
        Integer setmealId = setmeal.getId();
        //给套餐添加检查组
        if (null != checkgroupIds) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId,checkgroupId);
            }
        }
    }

    /** 功能描述:
    分页查询
    * @return: com.itheima.health.entity.PageResult<com.itheima.health.pojo.Setmeal>
    * @Author: admin
    * @Date: 2020-10-25 23:06
    */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //查询条件模糊查询
        if(StringUtil.isNotEmpty(queryPageBean.getQueryString())){
            //拼接
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //分页
        Page<Setmeal> page =  setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(page.getTotal(),page.getResult());
    }

    /** 功能描述:
    通过id查询套餐
    * @return: com.itheima.health.pojo.Setmeal
    * @Author: admin
    * @Date: 2020-10-25 23:56
    */

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /** 功能描述:
     通过套餐id查询所有检查组ids
     * @return: com.itheima.health.pojo.Setmeal
     * @Author: admin
     * @Date: 2020-10-25 23:56
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    /** 功能描述:
     更新套餐信息
     --先更新套餐信息
     --删除旧关系
     --添加新关系
     * @return: com.itheima.health.pojo.Setmeal
     * @Author: admin
     * @Date: 2020-10-25 23:56
     */
    @Transactional
    @Override
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.update(setmeal);
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }

        }
    }

    /** 功能描述:
     删除套餐
     --是否存在被订单关联 ->有则失败
     --删除与检查组关系
     --删除套餐

     * @return: com.itheima.health.pojo.Setmeal
     * @Author: admin
     * @Date: 2020-10-25 23:56
     */
    @Override
    public void delete(Integer id){
        int count = setmealDao.findOrderCountBySetmealId(id);
        if(count>0){
            throw  new HealthException("已经有订单使用此套餐");
        }
        setmealDao.deleteSetmealCheckGroup(id);

        setmealDao.deleteById(id);

    }


    /** 功能描述:
    删除七牛云上垃圾图片
    * @return:
    * @Author: admin
    * @Date: 2020-10-26 17:28
    */
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }
}
