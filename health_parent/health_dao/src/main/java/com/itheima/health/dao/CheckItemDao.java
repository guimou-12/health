package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
     Page<CheckItem> findByCondition(String queryString);
    List<CheckItem> findAll();

    void add(CheckItem checkItem);

    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    int findCountByCheckItemId(int id);


    void deleteById(int id);

    CheckItem findById(int id);

    void update(CheckItem checkItem);
}
