package com.r2.config;

import com.r2.common.JacksonObjectMapper;
import com.r2.common.R;
import com.r2.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
//正常来说只有template和static下的文件才能被访问到，但是我们在这里配置了WebMvcConfig来实现访问resources/backend下的文件
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override  //设置静态
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射");
        //只要访问路径是/backend下的，都会去classpath:/backend/下寻找对应的资源
        //举例：访问路径为：http://localhost:8080/backend/index.html
        //实际访问路径为：classpath:/backend/index.html，这里classpath指的是resources目录
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    //拓展MVC框架的消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("开始进行消息转换器的拓展...");
        //创建新的消息转换器，把返回的R对象转换成JSON
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层将Java对象转成JSON
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中，并且排到第一位
        converters.add(0, messageConverter);
    }

}
