package net.ken.springbootmall.dao;

import net.ken.springbootmall.dto.UserRegisterRequest;
import net.ken.springbootmall.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User getUserByEmail(String email);
}
