package com.r2;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j  //日志类注解
@SpringBootApplication  //标注为springboot启动类
@ServletComponentScan //扫描过滤器filter/LoginCheckFilter来使其生效
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        System.out.println("Hello Reggie!");
        SpringApplication.run(ReggieApplication.class, args);

        log.info("Hello Reggie!");
    }
}
