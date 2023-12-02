package com.r2.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.entity.SetmealDish;
import com.r2.mapper.SetmealDishMapper;
import com.r2.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper,SetmealDish> implements SetmealDishService {
}
