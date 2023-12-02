package com.r2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.r2.entity.Category;
import com.r2.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
