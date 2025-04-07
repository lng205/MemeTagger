package com.meme.service;

import com.meme.entity.MemeVote;
import com.meme.mapper.MemeVoteMapper;
import com.meme.util.UserContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemeVoteService {

    private final MemeVoteMapper memeVoteMapper;
    private final RedisLikeService redisLikeService;

    public MemeVoteService(MemeVoteMapper memeVoteMapper, RedisLikeService redisLikeService) {
        this.memeVoteMapper = memeVoteMapper;
        this.redisLikeService = redisLikeService;
    }

    /**
     * Toggle a vote (like) on a meme for the current user
     * @param memeId The meme ID
     * @return true if the meme is now liked, false if unliked
     */
    @Transactional
    public boolean toggleVote(Integer memeId) {
        // Get user ID from context
        Long userIdLong = UserContextUtil.getUserId();
        if (userIdLong == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Integer userId = userIdLong.intValue();
        
        // Toggle like in Redis (fast in-memory operation)
        boolean liked = redisLikeService.toggleLike(memeId, userId);
        
        // Sync with database for persistence (background operation)
        if (liked) {
            // User has not voted, so add a vote (like)
            MemeVote vote = new MemeVote(memeId, userId);
            memeVoteMapper.insert(vote);
        } else {
            // User already voted, so remove the vote (unlike)
            memeVoteMapper.delete(memeId, userId);
        }
        
        return liked;
    }
    
    /**
     * Get the like count for a meme
     * @param memeId The meme ID
     * @return The number of likes
     */
    public Integer getLikeCount(Integer memeId) {
        return redisLikeService.getLikeCount(memeId);
    }
    
    /**
     * Check if a user has liked a meme
     * @param memeId The meme ID
     * @param userId The user ID
     * @return true if the user liked the meme, false otherwise
     */
    public boolean hasUserLiked(Integer memeId, Integer userId) {
        return redisLikeService.hasUserLiked(memeId, userId);
    }
}
