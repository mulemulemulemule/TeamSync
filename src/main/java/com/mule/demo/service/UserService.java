package com.mule.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mule.demo.entity.User;
import com.mule.demo.model.dto.UserRegisterDTO;
import com.mule.demo.model.dto.UserLoginDTO;
/**
 * 用户业务层接口 (User Service Interface)
 */
public interface UserService extends IService<User> {
User register(UserRegisterDTO userRegisterDTO);
String login(UserLoginDTO userLoginDTO);
}