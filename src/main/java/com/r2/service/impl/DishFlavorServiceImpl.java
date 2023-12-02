package com.r2.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.entity.Dish;
import com.r2.entity.DishFlavor;
import com.r2.mapper.DishFlavorMapper;
import com.r2.mapper.DishMapper;
import com.r2.service.DishFlavorService;
import com.r2.service.DishService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
