package com.r2.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.r2.common.R;
import com.r2.dto.DishDto;
import com.r2.entity.Category;
import com.r2.entity.Dish;
import com.r2.entity.DishFlavor;
import com.r2.service.CategoryService;
import com.r2.service.DishFlavorService;
import com.r2.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService ds;

    @Autowired
    private DishFlavorService dfs;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品:{}", dishDto);
        ds.SaveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("查询菜品列表:page={},pageSize={},name={}", page, pageSize, name);

        //构造分页构造器对象
        Page<Dish> dishInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        ds.page(dishInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(dishInfo, dishDtoPage, "records");

        List<Dish> records = dishInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//category id
            // search for category name by category id
            Category category = categoryService.getById(categoryId) ;

            if(category != null){
                String categoryName = category.getName () ;
                dishDto.setCategoryName(categoryName);
            }

            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dfs.list(wrapper);
            //SQL: select * from dish_flavor where dish_id = #{dishId}
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")  //根据id查询菜品信息和口味
    public R<DishDto> get(@PathVariable Long id){
        DishDto dto = ds.getByIdWithFlavor(id);
        return R.success(dto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("新增菜品:{}", dishDto);
        ds.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");

    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        log.info("查询菜品列表");
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId, dish.getCategoryId());
        //0 is off the shelf, 1 is on sale
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> records = ds.list(queryWrapper);

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//category id
            // search for category name by category id
            Category category = categoryService.getById(categoryId) ;

            if(category != null){
                String categoryName = category.getName () ;
                dishDto.setCategoryName(categoryName);
            }

            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dfs.list(wrapper);
            //SQL: select * from dish_flavor where dish_id = #{dishId}
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(list);
    }


}
