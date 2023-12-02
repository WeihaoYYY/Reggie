package com.r2.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.dto.DishDto;
import com.r2.entity.Dish;

import com.r2.entity.DishFlavor;
import com.r2.mapper.DishMapper;
import com.r2.service.DishFlavorService;
import com.r2.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dfs;

    //由于该方法涉及到多个表的操作,所以需要加上事务注解
    @Transactional
    public void SaveWithFlavor(DishDto ddto) {
        //保存基本的菜品信息到菜品表
        this.save(ddto);

        Long id = ddto.getId();

        //保存菜品和口味的关系到菜品口味表
        List<DishFlavor> flavors = ddto.getFlavors();
        //.stream()是将集合转化为流,.map是将流中的元素计算或者转换(此处用map为流中的id赋值),.collect是将流转为集合
        flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味到菜品口味表dish_flavor
        dfs.saveBatch(flavors);

    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //根据id查询菜品基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        //将菜品基本信息拷贝到菜品dto中
        BeanUtils.copyProperties(dish, dishDto);

        //查询dish_flavor菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavors = dfs.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Transactional
    public void updateWithFlavor(DishDto ddto) {
        //更新菜品基本信息
        this.updateById(ddto);

        //清理菜品对应的口味信息，dish_flavor表的delete操作

        //更新菜品口味信息，dish_flavor表的insert操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, ddto.getId());

        dfs.remove(queryWrapper);

        //添加当前提交过来的口味数据，dish_flavor表的insert操作
        List<DishFlavor> flavors = ddto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(ddto.getId());
            return item;
        }).collect(Collectors.toList());

        dfs.saveBatch(flavors);
    }
}
