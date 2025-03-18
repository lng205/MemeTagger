package com.meme.controller;

import com.meme.dto.UserDTO;
import com.meme.result.Result;
import com.meme.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        try {
            userService.login(userDTO);
        } catch (LoginException e) {
            logger.error("Login Exception", e);
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    @PutMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        try {
            userService.register(userDTO);
        } catch (AccountException e) {
            logger.error("Account Exception", e);
            return Result.error(e.getMessage());
        }
        return Result.success();
    }
}
