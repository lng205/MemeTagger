package com.meme.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Service
public class RedisLikeService {

    private static final Logger log = LoggerFactory.getLogger(RedisLikeService.class);
    private final StringRedisTemplate redisTemplate;
    private final MemeVoteService memeVoteService;
    
    // Simple key patterns
    private static final String LIKES_COUNT_KEY = "likes:count:";
    private static final String USER_LIKE_KEY = "user:like:";
    
    // Queue for pending database operations
    private static final String PENDING_LIKES_QUEUE = "queue:likes";
    private static final String PENDING_UNLIKES_QUEUE = "queue:unlikes";

    public RedisLikeService(StringRedisTemplate redisTemplate, MemeVoteService memeVoteService) {
        this.redisTemplate = redisTemplate;
        this.memeVoteService = memeVoteService;
    }
    
    /**
     * Toggle like status for a meme in Redis
     * Uses write-behind pattern: Redis is updated immediately, database asynchronously
     * @return true if the meme is now liked, false if unliked
     */
    public boolean toggleLike(Integer memeId, Integer userId) {
        if (memeId == null || userId == null) {
            return false;
        }
        
        try {
            String userLikeKey = USER_LIKE_KEY + userId + ":" + memeId;
            String likesCountKey = LIKES_COUNT_KEY + memeId;
            String queueValue = userId + ":" + memeId;
            
            Boolean hasLiked = redisTemplate.hasKey(userLikeKey);
            
            if (Boolean.TRUE.equals(hasLiked)) {
                // Unlike: Remove key and decrement count
                redisTemplate.delete(userLikeKey);
                redisTemplate.opsForValue().decrement(likesCountKey);
                
                // Add to unlike queue
                redisTemplate.opsForList().rightPush(PENDING_UNLIKES_QUEUE, queueValue);
                return false;
            } else {
                // Like: Set key and increment count
                redisTemplate.opsForValue().set(userLikeKey, "1");
                redisTemplate.opsForValue().increment(likesCountKey);
                
                // Add to like queue
                redisTemplate.opsForList().rightPush(PENDING_LIKES_QUEUE, queueValue);
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
    
    /**
     * Process pending likes (runs every 15 seconds)
     */
    @Scheduled(fixedRate = 15000)
    public void processPendingLikes() {
        processPendingOperations(PENDING_LIKES_QUEUE, true);
    }
    
    /**
     * Process pending unlikes (runs every 15 seconds)
     */
    @Scheduled(fixedRate = 15000)
    public void processPendingUnlikes() {
        processPendingOperations(PENDING_UNLIKES_QUEUE, false);
    }
    
    /**
     * Process pending operations from queues
     */
    private void processPendingOperations(String queueKey, boolean isLike) {
        try {
            // Get batch size (process up to 20 items at once)
            int batchSize = 20;
            List<String> batch = redisTemplate.opsForList().range(queueKey, 0, batchSize - 1);
            
            if (batch == null || batch.isEmpty()) {
                return;
            }
            
            for (String item : batch) {
                try {
                    String[] parts = item.split(":");
                    if (parts.length == 2) {
                        Integer userId = Integer.parseInt(parts[0]);
                        Integer memeId = Integer.parseInt(parts[1]);
                        
                        // Process in database
                        if (isLike) {
                            memeVoteService.addLike(memeId, userId);
                        } else {
                            memeVoteService.removeLike(memeId, userId);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Error processing {} item {}: {}", isLike ? "like" : "unlike", item, e.getMessage());
                }
                
                // Remove from queue
                redisTemplate.opsForList().leftPop(queueKey);
            }
        } catch (Exception e) {
            log.error("Error processing {} queue: {}", isLike ? "like" : "unlike", e.getMessage());
        }
    }
    
    /**
     * Get the Redis template
     */
    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
