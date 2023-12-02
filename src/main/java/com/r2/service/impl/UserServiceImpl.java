package com.r2.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.entity.User;
import com.r2.mapper.UserMapper;
import com.r2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {

}
