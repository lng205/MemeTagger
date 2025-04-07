package com.meme.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meme.dto.MemePageQueryDTO;
import com.meme.dto.PublicMemePageQueryDTO;
import com.meme.entity.Meme;
import com.meme.mapper.MemeMapper;
import com.meme.mapper.TagMapper;
import com.meme.result.PageResult;
import com.meme.vo.MemeVO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MemeService {

    private final MemeMapper memeMapper;
    private final TagMapper tagMapper;

    public MemeService(MemeMapper memeMapper, TagMapper tagMapper) {
        this.memeMapper = memeMapper;
        this.tagMapper = tagMapper;
    }

    public Meme save(String url, Integer userId) {
        Meme meme = new Meme(url, userId);
        memeMapper.insert(meme);
        return meme;
    }

    public MemeVO getMeme(Integer id) {
        MemeVO meme = memeMapper.getMeme(id);
        meme.setTags(tagMapper.getTagsByMemeId(meme.getId()));
        return meme;
    }

    public PageResult<MemeVO> getMemePageByUser(MemePageQueryDTO memePageQueryDTO) {
        PageHelper.startPage(memePageQueryDTO.page(), memePageQueryDTO.pageSize());
        Page<Integer> memeIds = memeMapper.getMemeIdsOnPageByUser(memePageQueryDTO.userId());
        
        // Handle empty page early
        if (memeIds == null || memeIds.isEmpty()) {
            return new PageResult<>(0L, Collections.emptyList());
        }
        
        List<MemeVO> memes = memeMapper.getMemes(memeIds);
        return new PageResult<>(memeIds.getTotal(), memes);
    }
    
    public PageResult<MemeVO> getAllMemesPage(PublicMemePageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.page(), queryDTO.pageSize());
        Page<Integer> memeIds = memeMapper.getAllMemeIdsOnPage();
        
        // Handle empty page early
        if (memeIds == null || memeIds.isEmpty()) {
            return new PageResult<>(0L, Collections.emptyList());
        }
        
        List<MemeVO> memes = memeMapper.getMemes(memeIds);
        return new PageResult<>(memeIds.getTotal(), memes);
    }
}
