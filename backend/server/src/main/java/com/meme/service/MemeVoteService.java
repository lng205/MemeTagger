package com.meme.service;

import com.meme.entity.MemeVote;
import com.meme.mapper.MemeVoteMapper;
import com.meme.util.UserContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;

@Service
public class MemeVoteService {

    private static final Logger log = LoggerFactory.getLogger(MemeVoteService.class);
    private final MemeVoteMapper memeVoteMapper;
    private final RedisLikeService redisLikeService;

    // Use @Lazy to break circular dependency
    public MemeVoteService(MemeVoteMapper memeVoteMapper, @Lazy RedisLikeService redisLikeService) {
        this.memeVoteMapper = memeVoteMapper;
        this.redisLikeService = redisLikeService;
    }

    /**
     * Toggle a vote (like) on a meme for the current user
     * Using write-behind pattern: Redis is updated immediately, database asynchronously
     * @param memeId The meme ID
     * @return true if the meme is now liked, false if unliked
     */
    public boolean toggleVote(Integer memeId) {
        // Get user ID from context using UserContextUtil
        Long userIdLong = UserContextUtil.getUserId();
        if (userIdLong == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Integer userId = userIdLong.intValue();
        
        // Toggle like in Redis and queue for async database update
        return redisLikeService.toggleLike(memeId, userId);
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
    
    /**
     * Add a like to the database
     * Called by RedisLikeService's background processor
     */
    public void addLike(Integer memeId, Integer userId) {
        try {
            // Use insertIfNotExists which handles the uniqueness constraint at the database level
            MemeVote vote = new MemeVote(memeId, userId);
            memeVoteMapper.insertIfNotExists(vote);
            log.debug("Added like for meme {} by user {}", memeId, userId);
        } catch (Exception e) {
            log.error("Error adding like for meme {} by user {}: {}", memeId, userId, e.getMessage());
        }
    }
    
    /**
     * Remove a like from the database
     * Called by RedisLikeService's background processor
     */
    public void removeLike(Integer memeId, Integer userId) {
        try {
            memeVoteMapper.delete(memeId, userId);
            log.debug("Removed like for meme {} by user {}", memeId, userId);
        } catch (Exception e) {
            log.error("Error removing like for meme {} by user {}: {}", memeId, userId, e.getMessage());
        }
    }
}
