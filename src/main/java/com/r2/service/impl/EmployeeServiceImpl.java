package com.r2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.r2.entity.Employee;
import com.r2.mapper.EmployeeMapper;
import com.r2.service.EmployeeService;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
