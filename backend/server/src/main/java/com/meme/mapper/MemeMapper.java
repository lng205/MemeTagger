package com.meme.mapper;

import com.github.pagehelper.Page;
import com.meme.entity.Meme;
import com.meme.vo.MemeVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface MemeMapper {

    @Insert("INSERT INTO meme(url, user_id) VALUES(#{url}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Meme meme);

    MemeVO getMeme(Integer id);

    Page<MemeVO> pageQuery(int userId);
}
