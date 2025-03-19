package com.meme.service;

import com.meme.mapper.MemeMapper;
import org.springframework.stereotype.Service;

@Service
public class MemeService {

    private final MemeMapper memeMapper;

    public MemeService(MemeMapper memeMapper) {
        this.memeMapper = memeMapper;
    }

    public void save(String url, Integer userId) {
        memeMapper.insert(url, userId);
    }
}
