package com.meme.controller;

import com.meme.dto.UserDTO;
import com.meme.result.Result;
import com.meme.service.TokenService;
import com.meme.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public Result<String> login(Authentication authentication) {
        return Result.success(tokenService.generateToken(authentication));
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

    @GetMapping
    public Result<String> test() {
        return Result.success("Hello World");
    }
}
