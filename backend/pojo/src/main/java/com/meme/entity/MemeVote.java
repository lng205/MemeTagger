package com.meme.entity;

import java.time.LocalDateTime;

public class MemeVote {
    private Integer id;
    private Integer memeId;
    private Integer userId;
    private LocalDateTime createdAt;

    public MemeVote() {}

    public MemeVote(Integer memeId, Integer userId) {
        this.memeId = memeId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemeId() {
        return memeId;
    }

    public void setMemeId(Integer memeId) {
        this.memeId = memeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
