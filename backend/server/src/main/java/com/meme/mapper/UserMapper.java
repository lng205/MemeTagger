package com.meme.mapper;

import com.meme.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(username, password) VALUES(#{username}, #{password})")
    void insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getByUsername(String username);
}
