package com.r2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.common.CustomException;
import com.r2.dto.SetmealDto;
import com.r2.entity.Category;
import com.r2.entity.Setmeal;
import com.r2.entity.SetmealDish;
import com.r2.mapper.CategoryMapper;
import com.r2.mapper.SetmealMapper;
import com.r2.service.CategoryService;
import com.r2.service.SetmealDishService;
import com.r2.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService sds;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto sd) {
        //保存套餐基本信息，操作setmeal，执行insert
        this.save(sd);
        List<SetmealDish> setmealDishes = sd.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(sd.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关系，操作setmeal_dish，执行insert
        sds.saveBatch(setmealDishes);


    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        sds.remove(lambdaQueryWrapper);
    }
}
