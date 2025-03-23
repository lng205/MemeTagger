package com.meme.dto;

public record MemePageQueryDTO(
    int page,
    int pageSize,
    int userId
) {
}
