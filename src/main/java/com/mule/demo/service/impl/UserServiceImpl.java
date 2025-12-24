package com.mule.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mule.demo.entity.User;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.mapper.UserMapper;
import com.mule.demo.service.UserService;

import cn.hutool.crypto.digest.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import com.mule.demo.model.dto.UserRegisterDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mule.demo.model.dto.UserLoginDTO;
import com.mule.demo.common.JwtUtils;
import org.springframework.web.multipart.MultipartFile;
import com.mule.demo.common.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {

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
    public Map<String, Object> login(UserLoginDTO userLoginDTO) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userLoginDTO.getUsername());
        User user = this.getOne(queryWrapper);
        if (user == null || !BCrypt.checkpw(userLoginDTO.getPassword(), user.getPassword())) {
            throw new ServiceException("Invalid username or password");
        }
        String token = JwtUtils.createToken(user.getId(), user.getUsername());
        String redisKey = "login:token:" + token;
        redisTemplate.opsForValue().set(redisKey, user.getId(), 24, TimeUnit.HOURS);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("avatar", user.getAvatar());
        return result;
    }
    @Override
    public String uploadAvatar(Long UserId,MultipartFile file) {
        User oldUser = this.getById(UserId);
        String oldAvatarUrl = oldUser.getAvatar();
        String url =minioUtils.upload(file);
        User user =new User();
        user.setId(UserId);
        user.setAvatar(url);
        boolean success=this.updateById(user);
        if(!success){
            throw new ServiceException("Failed to update user avatar");
}
if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
    minioUtils.removeFile(oldAvatarUrl);
}
        return url;
    }
    @Override
    public void logout(String token) {
         String redisKey = "login:token:" + token;
        redisTemplate.delete(redisKey);
    }
}