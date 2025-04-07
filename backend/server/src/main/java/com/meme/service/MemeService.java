package com.meme.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meme.dto.MemePageQueryDTO;
import com.meme.dto.PublicMemePageQueryDTO;
import com.meme.entity.Meme;
import com.meme.mapper.MemeMapper;
import com.meme.mapper.TagMapper;
import com.meme.result.PageResult;
import com.meme.util.UserContextUtil;
import com.meme.vo.MemeVO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MemeService {

    private final MemeMapper memeMapper;
    private final TagMapper tagMapper;
    private final MemeVoteService memeVoteService;

    public MemeService(MemeMapper memeMapper, TagMapper tagMapper, MemeVoteService memeVoteService) {
        this.memeMapper = memeMapper;
        this.tagMapper = tagMapper;
        this.memeVoteService = memeVoteService;
    }

    public Meme save(String url, Integer userId) {
        Meme meme = new Meme(url, userId);
        memeMapper.insert(meme);
        return meme;
    }

    public MemeVO getMeme(Integer id) {
        MemeVO meme = memeMapper.getMeme(id);
        meme.setTags(tagMapper.getTagsByMemeId(meme.getId()));
        
        // Add vote information for the current user
        addVoteInfo(meme);
        
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
        
        // Add vote information for each meme
        addVoteInfoToList(memes);
        
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
        
        // Add vote information for each meme
        addVoteInfoToList(memes);
        
        return new PageResult<>(memeIds.getTotal(), memes);
    }
    
    /**
     * Add vote information to a MemeVO object
     * 
     * @param meme The meme to enhance with vote information
     */
    private void addVoteInfo(MemeVO meme) {
        if (meme == null) return;
        
        // Get vote count
        Integer likeCount = memeVoteService.getLikeCount(meme.getId());
        meme.setLikeCount(likeCount);
        
        // Get whether current user has liked this meme
        Long currentUserId = UserContextUtil.getUserId();
        if (currentUserId != null) {
            boolean userLiked = memeVoteService.hasUserLiked(meme.getId(), currentUserId.intValue());
            meme.setUserLiked(userLiked);
        } else {
            meme.setUserLiked(false);
        }
    }
    
    /**
     * Add vote information to a list of MemeVO objects
     * 
     * @param memes The list of memes to enhance with vote information
     */
    private void addVoteInfoToList(List<MemeVO> memes) {
        if (memes == null || memes.isEmpty()) return;
        
        for (MemeVO meme : memes) {
            addVoteInfo(meme);
        }
    }
}
