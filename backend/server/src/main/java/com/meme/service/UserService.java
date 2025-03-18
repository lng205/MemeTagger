package com.meme.service;

import com.meme.constant.MessageConstant;
import com.meme.dto.UserDTO;
import com.meme.entity.User;
import com.meme.mapper.UserMapper;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

@Service
public class UserService {

    UserMapper userMapper;
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void login(UserDTO userDTO) throws LoginException {
        User user = userMapper.getByUsername(userDTO.username());
        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (!user.getPassword().equals(userDTO.password())) {
            throw new AuthException(MessageConstant.PASSWORD_ERROR);
        }
    }

    public void register(UserDTO userDTO) throws AccountException {
        if (userMapper.getByUsername(userDTO.username()) != null) {
            throw new AccountException(MessageConstant.ACCOUNT_EXIST);
        }
        User user = new User(userDTO.username(), userDTO.password());
        userMapper.insert(user);
    }
}
