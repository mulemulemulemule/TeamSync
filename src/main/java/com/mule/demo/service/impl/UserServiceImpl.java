package com.mule.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mule.demo.entity.User;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.UserMapper;
import com.mule.demo.service.UserService;

import cn.hutool.crypto.digest.BCrypt;

import org.springframework.stereotype.Service;
import com.mule.demo.model.dto.UserRegisterDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mule.demo.model.dto.UserLoginDTO;
import com.mule.demo.common.JwtUtils;
import org.springframework.web.multipart.MultipartFile;
import com.mule.demo.common.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户业务层实现类 (User Service Implementation)
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private MinioUtils minioUtils;

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        // 注册逻辑
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userRegisterDTO.getUsername());
        User existingUser = this.getOne(queryWrapper);
        if (existingUser != null) {
            throw new ServiceException("Username ... is already taken");
        }

        User newUser = new User();
        newUser.setUsername(userRegisterDTO.getUsername());
        newUser.setEmail(userRegisterDTO.getEmail());
        String encryptedPassword =BCrypt.hashpw(userRegisterDTO.getPassword());
        newUser.setPassword(encryptedPassword);

        this.save(newUser);
        return newUser;
    }
    @Override
    public String login(UserLoginDTO userLoginDTO) {
        // 登录逻辑
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userLoginDTO.getUsername());
        User user = this.getOne(queryWrapper);
        if (user == null || !BCrypt.checkpw(userLoginDTO.getPassword(), user.getPassword())) {
            throw new ServiceException("Invalid username or password");
        }
        return JwtUtils.createToken(user.getId(), user.getUsername());
    }
    @Override
    public String uploadAvatar(Long UserId,MultipartFile file) {
        // 上传头像逻辑
        String url =minioUtils.upload(file);
        User user =new User();
        user.setId(UserId);
        user.setAvatar(url);
        boolean success=this.updateById(user);
        if(!success){
            throw new ServiceException("Failed to update user avatar");
}
        return url;
    }
}