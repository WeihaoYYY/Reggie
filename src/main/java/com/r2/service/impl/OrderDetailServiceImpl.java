package com.r2.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.entity.OrderDetail;
import com.r2.mapper.OrderDetailMapper;
import com.r2.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}