package com.meme.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meme.dto.MemePageQueryDTO;
import com.meme.entity.Meme;
import com.meme.mapper.MemeMapper;
import com.meme.mapper.TagMapper;
import com.meme.vo.MemeVO;
import org.springframework.stereotype.Service;

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

    public List<MemeVO> getMemePageByUser(MemePageQueryDTO memePageQueryDTO) {
        // TODO return total meme count
        PageHelper.startPage(memePageQueryDTO.page(), memePageQueryDTO.pageSize());
        Page<Integer> memeIds = memeMapper.getMemeIdsOnPageByUser(memePageQueryDTO.userId());

        return memeMapper.getMemes(memeIds.getResult());
    }
}
