package com.r2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.r2.common.BaseContext;
import com.r2.common.R;
import com.r2.entity.Category;
import com.r2.entity.Employee;
import com.r2.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增分类:{}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        log.info("page: {}, size: {}", page, pageSize);
        //创建分页构造器
        Page pageInfo = new Page<>(page, pageSize);
        //构造条件构造器 SQL查询
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        //按照更新时间降序排列
        queryWrapper.orderByAsc(Category::getSort);
        //调用分页查询方法
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类:{}", ids);
        categoryService.remove(ids);
        return R.success("删除分类成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类:{}", category);
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    @GetMapping("/list")  //新增菜品时，动态查询菜品分类种类
    public R<List<Category>> list(Category category) {
        log.info("查询分类列表");
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        return R.success(categoryService.list(queryWrapper));
    }

}
