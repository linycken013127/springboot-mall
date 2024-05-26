package net.ken.springbootmall.service;

import net.ken.springbootmall.dto.UserLoginRequest;
import net.ken.springbootmall.dto.UserRegisterRequest;
import net.ken.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
}
