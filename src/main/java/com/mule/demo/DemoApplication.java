package com.mule.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mule.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ  TeamSync 启动成功   ヾ(◍°∇°◍)ﾉﾞ");
    }

}