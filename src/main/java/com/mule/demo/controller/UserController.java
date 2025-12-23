package com.mule.demo.controller;

import com.mule.demo.common.Result;
import com.mule.demo.common.UserContext;
import com.mule.demo.entity.User;
import com.mule.demo.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.mule.demo.model.dto.UserRegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import com.mule.demo.model.dto.UserLoginDTO;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/user")
@Tag(name ="用户管理")
public class UserController {
    @Autowired
    private UserService userService;


    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<User> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        User user =userService.register(userRegisterDTO);
        return Result.success(user);
    }


    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String,String>> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        String token =userService.login(userLoginDTO);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("username", userLoginDTO.getUsername());
        return Result.success(data);
    }


    @Operation(summary = "头像上传")
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {       
if(file.isEmpty()){
    return Result.error("file is empty");
}
Long userId = UserContext.getUserId();
String url = userService.uploadAvatar(userId,file);
return Result.success(url);
    }
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<String> logout(jakarta.servlet.http.HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null) {
            userService.logout(token);
    }
    return Result.success("logout success");
}
}