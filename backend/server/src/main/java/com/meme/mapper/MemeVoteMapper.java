package com.meme.mapper;

import com.meme.entity.MemeVote;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemeVoteMapper {
    
    @Insert("INSERT INTO meme_vote (meme_id, user_id, created_at) VALUES (#{memeId}, #{userId}, NOW())")
    void insert(MemeVote memeVote);
    
    @Insert("INSERT IGNORE INTO meme_vote (meme_id, user_id, created_at) VALUES (#{memeId}, #{userId}, NOW())")
    void insertIfNotExists(MemeVote memeVote);
    
    @Delete("DELETE FROM meme_vote WHERE meme_id = #{memeId} AND user_id = #{userId}")
    void delete(@Param("memeId") Integer memeId, @Param("userId") Integer userId);
    
    @Select("SELECT COUNT(*) FROM meme_vote WHERE meme_id = #{memeId}")
    Integer countVotesByMemeId(@Param("memeId") Integer memeId);
    
    @Select("SELECT COUNT(*) > 0 FROM meme_vote WHERE meme_id = #{memeId}")
    Integer countLikes(@Param("memeId") Integer memeId);
    
    @Select("SELECT COUNT(*) > 0 FROM meme_vote WHERE meme_id = #{memeId} AND user_id = #{userId}")
    Boolean hasUserVoted(@Param("memeId") Integer memeId, @Param("userId") Integer userId);
}
