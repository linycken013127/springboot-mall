package net.ken.springbootmall.controller;

import jakarta.validation.Valid;
import net.ken.springbootmall.dto.UserLoginRequest;
import net.ken.springbootmall.dto.UserRegisterRequest;
import net.ken.springbootmall.model.User;
import net.ken.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("users/register")
    public ResponseEntity<User> register(
            @RequestBody @Valid UserRegisterRequest userRegisterRequest
    ) {
        Integer userId = userService.register(userRegisterRequest);
        User user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("users/login")
    public ResponseEntity<User> login(
            @RequestBody @Valid UserLoginRequest userLoginRequest
    ) {
        User user = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
