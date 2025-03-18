package com.meme.mapper;

import com.meme.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user(username, password) values(#{username}, #{password})")
    void insert(User user);

    @Select("select * from user where username = #{username}")
    User getByUsername(String username);
}
