package com.r2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.r2.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
