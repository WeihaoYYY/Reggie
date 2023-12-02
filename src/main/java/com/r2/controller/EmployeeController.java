package com.r2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.r2.common.R;
import com.r2.entity.Employee;
import com.r2.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;  //


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    //用RequestBody接受json的数据转换成Employee对象
    //用HttpServletRequest来把用户ID存入session方便后续操作
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("employee: {}", employee.toString());

        //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据用户名查询用数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //Employee::getUsername就相当于创建一个Employee对象并调用其getUsername方法
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.判断用户是否存在
        if (emp == null) {
            return R.error("用户不存在");
        }

        //4.判断密码是否正确
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        //5.查看用户是否被禁用
        if (emp.getStatus() == 0) {
            return R.error("用户被禁用");
        }

        //6.登陆成功，将用户信息存入session, 不存session，后续跳转其他页面，你怎么知道它有没有登录呢？
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //退出登录，删除session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置初始状态
        employee.setStatus(1);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((long)request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((long)request.getSession().getAttribute("employee"));

        log.info("新增员工信息: {}", employee.toString());

        employeeService.save(employee);
        return R.success("保存成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page: {}, size: {}, name: {}", page, pageSize, name);
        //创建分页构造器
        Page pageInfo = new Page<>(page, pageSize);
        //构造条件构造器 SQL查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //根据员工姓名模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //按照更新时间降序排列
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //调用分页查询方法
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("修改员工信息: {}", employee.toString());
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser((long)request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")  //根据id查询员工信息
    public R<Employee> getById(@PathVariable Long id) {
        log.info("查询到员工信息: {}", id);
        Employee employee = employeeService.getById(id);
        if(employee == null)
            return R.error("员工不存在");
        return R.success(employee);
    }

}
