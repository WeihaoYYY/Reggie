package com.r2.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.r2.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
