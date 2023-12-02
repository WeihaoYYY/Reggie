package com.r2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.r2.dto.DishDto;
import com.r2.entity.Category;
import com.r2.entity.Dish;

public interface DishService extends IService<Dish>{

    public void SaveWithFlavor(DishDto ddto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto ddto);
}
