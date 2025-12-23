package com.mule.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 * <p>
 * 这是整个项目的入口。运行 main 方法就会启动内嵌的 Tomcat 服务器。
 * </p>
 */
@SpringBootApplication // 这是一个组合注解，包含了 @Configuration, @EnableAutoConfiguration, @ComponentScan
@MapperScan("com.mule.demo.mapper") // 核心配置：告诉 MyBatis 去哪里扫描 Mapper 接口
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  TeamSync 启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}