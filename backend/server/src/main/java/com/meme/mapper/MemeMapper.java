package com.meme.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemeMapper {

    @Insert("INSERT INTO meme(url, user_id) VALUES(#{url}, #{userId})")
    void insert(@Param("url") String url, @Param("userId") Integer userId);
}
