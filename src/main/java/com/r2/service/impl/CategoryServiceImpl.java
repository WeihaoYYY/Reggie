package com.r2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.common.CustomException;
import com.r2.entity.Category;
import com.r2.entity.Dish;
import com.r2.entity.Employee;
import com.r2.entity.Setmeal;
import com.r2.mapper.CategoryMapper;
import com.r2.mapper.DishMapper;
import com.r2.mapper.EmployeeMapper;
import com.r2.service.CategoryService;
import com.r2.service.DishService;
import com.r2.service.EmployeeService;
import com.r2.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    @Override
    public void remove(Long id) {
        //查询当前分类下是否关联了菜品，如果已经关联了，抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        //查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        //查询返回数目的数量
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }


        //查询当前分类下是否关联了套餐，如果已经关联了，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除
        super.removeById(id);

    }
}
