package com.mule.demo.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mule.demo.common.JwtUtils;
import com.mule.demo.entity.User;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.UserMapper;
import com.mule.demo.model.dto.UserLoginDTO;
import com.mule.demo.model.dto.UserRegisterDTO;
import com.mule.demo.service.RedisService;
import com.mule.demo.service.MinioService;
import com.mule.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.HashMap;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MinioService minioService;

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        if (this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, userRegisterDTO.getUsername())) > 0) {
            throw new ServiceException("Username already taken");
        }
        User newUser = new User();
        newUser.setUsername(userRegisterDTO.getUsername());
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setPassword(BCrypt.hashpw(userRegisterDTO.getPassword()));
        this.save(newUser);
        return newUser;
    }

    @Override
    public Map<String, Object> login(UserLoginDTO userLoginDTO) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userLoginDTO.getUsername()));
        if (user == null || !BCrypt.checkpw(userLoginDTO.getPassword(), user.getPassword())) {
            throw new ServiceException("Invalid username or password");
        }
        String token = JwtUtils.createToken(user.getId(), user.getUsername());
        redisService.saveToken(token, user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("avatar", user.getAvatar());
        return result;
    }

    @Override
    public void logout(String token) {
        redisService.deleteToken(token);
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) {
        User user = this.getById(userId);
        String oldUrl = user.getAvatar();
        String newUrl = minioService.upload(file);
        user.setAvatar(newUrl);
        this.updateById(user);
        if (oldUrl != null) minioService.delete(oldUrl);
        return newUrl;
    }
}