package com.r2.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.entity.ShoppingCart;
import com.r2.mapper.ShoppingCartMapper;
import com.r2.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
