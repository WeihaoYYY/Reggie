package com.r2.controller;


import com.r2.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public  R<String> upload(MultipartFile file) {
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info("上传文件:{}", file.getOriginalFilename());

        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        //使用UUID生成文件名，防止文件名相同造成的文件覆盖
        String randName = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);
        //如果目标文件所在的目录不存在，则创建父目录
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + randName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(randName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        log.info("下载文件:{}", name);

        //输入流，通过输入流读取文件
        try {
            FileInputStream fis = new FileInputStream(new File(basePath + name));

            response.setContentType("image/jpeg");

            //输出流，通过输出流写入文件到浏览器上以展示图片
            response.getOutputStream().write(fis.readAllBytes());

            fis.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
