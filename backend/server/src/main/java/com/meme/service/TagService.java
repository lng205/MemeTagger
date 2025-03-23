package com.meme.service;

import com.meme.entity.Tag;
import com.meme.mapper.TagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagMapper tagMapper;

    public TagService(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public void setMemeTags(Integer memeId, List<String> tags) {
        tagMapper.deleteTagsByMemeId(memeId);
        for (String tag : tags) {
            tagMapper.insert(tag);
            tagMapper.insertMemeTag(memeId, tagMapper.getTagIdByName(tag));
        }
    }

    public List<Tag> getTagsByMemeId(Integer id) {
        return tagMapper.getTagsByMemeId(id);
    }
}
