package com.mule.demo.controller;

import com.mule.demo.common.Result;
import com.mule.demo.entity.User;
import com.mule.demo.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.mule.demo.model.dto.UserRegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import com.mule.demo.model.dto.UserLoginDTO;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 用户控制器 (User Controller)
 */
@RestController // 1. 告诉 Spring 这是一个控制器; 2. 所有方法默认返回 JSON 数据 (不是页面)
@RequestMapping("/user") // 定义该控制器的基础路径。所有接口都以 /user 开头
@Tag(name ="用户管理")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询所有用户接口
     */
    @Operation(summary = "查询所有用户") // Swagger 注解，用于生成接口文档
    @GetMapping("/list")
    public Result<List<User>> listAllUsers() {
        // 调用 Service 继承自 MyBatis Plus 的 list() 方法
        // 它会自动执行 SELECT * FROM sys_user WHERE deleted=0
        List<User> userList = userService.list();
        
        // 将结果放入 Result 包装盒中返回
        return Result.success(userList);
    }
    /**
     * 用户注册接口
     */
    @Operation(summary = "用户注册") // Swagger 注解，用于生成接口文档
    @PostMapping("/register")
    public Result<User> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        User user =userService.register(userRegisterDTO);
        return Result.success(user);
    }
    /**
     * 用户登录接口
     */
    @Operation(summary = "用户登录") // Swagger 注解，用于生成接口文档
    @PostMapping("/login")
    public Result<Map<String,String>> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        String token =userService.login(userLoginDTO);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("username", userLoginDTO.getUsername());
        return Result.success(data);
}
}