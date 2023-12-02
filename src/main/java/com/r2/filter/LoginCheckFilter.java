package com.r2.filter;

import ch.qos.logback.classic.pattern.Util;
import com.alibaba.fastjson.JSON;
import com.r2.common.BaseContext;
import com.r2.common.R;
import com.r2.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        log.info("拦截到请求 {}", req.getRequestURI());

        //1.获取请求的URI
        String uri = req.getRequestURI();
        //注意这里是URI的路径，对应的是类的mapping地址，而不是存放类文件的路径
        String[] urls = new String[]{  //不需要登录就可以访问的页面
                "/employee/login",  //登录
                "/employee/logout",  //退出登录
                "/backend/**",
                "/backend/page/demo/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, uri);

        //3、如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",uri);
            chain.doFilter(request,response);
            return;
        }

        //4.1、判断登录状态，如果已登录，则直接放行
        if(req.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}",req.getSession().getAttribute("employee"));
            Long empId = (Long) req.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            chain.doFilter(request,response);
            return;
        }

        //4.2、判断登录状态，如果已登录，则直接放行
        if(req.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",req.getSession().getAttribute("user"));

            User user = (User) req.getSession().getAttribute("user");

            BaseContext.setCurrentId(user.getId());
            chain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据，这里的NOTLOGIN对应前端的重定向代码
        resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
