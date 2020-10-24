package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service(interfaceClass=CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {

        return checkItemDao.findAll();
    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询(基于mybatis分页插件)
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //limit(a,b) a:开始数据 b：分页大小
        //获取a
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        //判断是否有查询条件
        if(!StringUtil.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }

        //分页
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());

        //封装到分页结果  总记录数  当前页结果
        PageResult<CheckItem> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        return pageResult;
    }

    //需要判断检查项是否与检查组关联
    @Override
    public void deleteById(int id) {
        int cnt = checkItemDao.findCountByCheckItemId(id);
        if(cnt > 0){
            throw new HealthException(MessageConstant.CHECKITEM_IN_USE);
        }
        checkItemDao.deleteById(id);
    }

    //
    @Override
    public CheckItem findById(int id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
