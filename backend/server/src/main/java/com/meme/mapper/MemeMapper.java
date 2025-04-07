package com.meme.mapper;

import com.github.pagehelper.Page;
import com.meme.entity.Meme;
import com.meme.vo.MemeVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemeMapper {

    @Insert("INSERT INTO meme(url, user_id) VALUES(#{url}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Meme meme);

    MemeVO getMeme(Integer id);

    List<MemeVO> getMemes(List<Integer> memeIds);

    @Select("SELECT meme.id FROM meme WHERE user_id = #{userId} ORDER BY meme.id DESC")
    Page<Integer> getMemeIdsOnPageByUser(int userId);
    
    @Select("SELECT meme.id FROM meme ORDER BY meme.id DESC")
    Page<Integer> getAllMemeIdsOnPage();
}
