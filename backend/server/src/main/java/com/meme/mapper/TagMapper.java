package com.meme.mapper;

import com.meme.entity.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TagMapper {

    @Select("SELECT * FROM tag LEFT JOIN meme_tag ON tag.id = meme_tag.tag_id WHERE meme_id = #{id}")
    List<Tag> getTagsByMemeId(Integer id);

    @Delete("DELETE FROM meme_tag WHERE meme_id = #{memeId}")
    void deleteTagsByMemeId(Integer memeId);

    @Insert("INSERT IGNORE INTO tag (name) VALUES (#{tag})")
    void insert(String tag);

    @Select("SELECT id FROM tag WHERE name = #{tag}")
    Integer getTagIdByName(String tag);

    @Insert("INSERT INTO meme_tag (meme_id, tag_id) VALUES (#{memeId}, #{tagId})")
    void insertMemeTag(@Param("memeId") Integer memeId, @Param("tagId") Integer tagId);
}
