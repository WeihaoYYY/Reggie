package com.r2.common;


// 基于ThreadLocal封装工具类，用于存储当前线程的用户信息，获取登录的用户ID
// Based on ThreadLocal encapsulation tool class,
// used to store the user information of the current thread and get the user ID of the login
public class BaseContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long userId) {
        threadLocal.set(userId);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }


}
