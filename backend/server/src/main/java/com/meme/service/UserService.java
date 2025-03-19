package com.meme.service;

import com.meme.constant.MessageConstant;
import com.meme.dto.UserDTO;
import com.meme.entity.User;
import com.meme.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;

@Service
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserDTO userDTO) throws AccountException {
        if (userMapper.getByUsername(userDTO.username()) != null) {
            throw new AccountException(MessageConstant.ACCOUNT_EXIST);
        }
        String hashedPassword = passwordEncoder.encode(userDTO.password());
        User user = new User(userDTO.username(), hashedPassword);
        userMapper.insert(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.getByUsername(username);
    }
}
