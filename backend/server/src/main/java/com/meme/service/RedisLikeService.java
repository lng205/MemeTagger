package com.meme.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RedisLikeService {

    private static final Logger log = LoggerFactory.getLogger(RedisLikeService.class);
    private final StringRedisTemplate redisTemplate;
    
    // Simple key patterns - store as simple Strings
    private static final String LIKES_COUNT_KEY = "likes:count:";
    private static final String USER_LIKE_KEY = "user:like:";

    public RedisLikeService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * Toggle like status for a meme
     */
    public boolean toggleLike(Integer memeId, Integer userId) {
        if (memeId == null || userId == null) {
            return false;
        }
        
        try {
            String userLikeKey = USER_LIKE_KEY + userId + ":" + memeId;
            String likesCountKey = LIKES_COUNT_KEY + memeId;
            
            Boolean hasLiked = redisTemplate.hasKey(userLikeKey);
            
            if (Boolean.TRUE.equals(hasLiked)) {
                // Unlike: Remove key and decrement count
                redisTemplate.delete(userLikeKey);
                Long newCount = redisTemplate.opsForValue().decrement(likesCountKey);
                if (newCount != null && newCount < 0) {
                    // Reset to 0 if negative
                    redisTemplate.opsForValue().set(likesCountKey, "0");
                }
                return false;
            } else {
                // Like: Set key and increment count
                redisTemplate.opsForValue().set(userLikeKey, "1");
                redisTemplate.opsForValue().increment(likesCountKey);
                return true;
            }
        } catch (Exception e) {
            log.error("Error toggling like for meme {} by user {}: {}", memeId, userId, e.getMessage());
            return false;
        }
    }
    
    /**
     * Get like count for a meme
     */
    public Integer getLikeCount(Integer memeId) {
        if (memeId == null) {
            return 0;
        }
        
        try {
            String key = LIKES_COUNT_KEY + memeId;
            String countStr = redisTemplate.opsForValue().get(key);
            
            if (countStr == null || countStr.isEmpty()) {
                return 0;
            }
            
            try {
                return Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                log.warn("Failed to parse like count for meme {}: {}", memeId, countStr);
                return 0;
            }
        } catch (Exception e) {
            log.error("Error getting like count for meme {}: {}", memeId, e.getMessage());
            return 0;
        }
    }
    
    /**
     * Check if user has liked a meme
     */
    public boolean hasUserLiked(Integer memeId, Integer userId) {
        if (memeId == null || userId == null) {
            return false;
        }
        
        try {
            String userLikeKey = USER_LIKE_KEY + userId + ":" + memeId;
            return Boolean.TRUE.equals(redisTemplate.hasKey(userLikeKey));
        } catch (Exception e) {
            log.error("Error checking if user {} liked meme {}: {}", userId, memeId, e.getMessage());
            return false;
        }
    }
}
