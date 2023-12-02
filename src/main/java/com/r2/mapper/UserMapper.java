package com.r2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.r2.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
