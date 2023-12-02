package com.r2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.r2.dto.SetmealDto;
import com.r2.entity.Category;
import com.r2.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal>{

    void saveWithDish(SetmealDto sd);

    void removeWithDish(List<Long> id);
}
