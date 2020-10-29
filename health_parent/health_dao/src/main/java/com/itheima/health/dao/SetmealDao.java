package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SetmealDao {
    void add(Setmeal setmeal);

    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    Page<Setmeal> findByCondition(String queryString);

    Setmeal findById(int id);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    void update(Setmeal setmeal);

    void deleteSetmealCheckGroup(Integer id);

    int findOrderCountBySetmealId(Integer id);

    void deleteById(Integer id);

    List<String> findImgs();

    List<Setmeal> findAll();

    Setmeal findDetailById(int id);

 /*   List<CheckGroup> findCheckGroupListBySetmealId(int id);

    List<CheckItem> findCheckItemByCheckGroupId(Integer id);*/


}
