package com.meme.entity;

import java.time.LocalDateTime;

public class Meme {

    private Integer id;
    private String url;
    private Integer userId;
    private LocalDateTime createdAt;

    public Meme() {}

    public Meme(String url, Integer userId) {
        this.url = url;
        this.userId = userId;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
