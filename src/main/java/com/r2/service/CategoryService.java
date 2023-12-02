package com.r2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.r2.entity.Category;
import com.r2.entity.Employee;

public interface CategoryService extends IService<Category>{

    public void remove(Long id);
}
