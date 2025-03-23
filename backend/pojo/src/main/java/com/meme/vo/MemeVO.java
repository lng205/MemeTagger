package com.meme.vo;

import com.meme.entity.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemeVO {
    private Integer id;
    private String url;
    private Integer userId;
    private String username;
    private LocalDateTime createdAt;
    private List<Tag> tags = new ArrayList<>();

    public MemeVO() {}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public List<Tag> getTags() {return tags;}
    public void setTags(List<Tag> tags) {this.tags = tags;}
}
