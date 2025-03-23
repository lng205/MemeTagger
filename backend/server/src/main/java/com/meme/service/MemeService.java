package com.meme.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meme.dto.MemePageQueryDTO;
import com.meme.entity.Tag;
import com.meme.mapper.MemeMapper;
import com.meme.mapper.TagMapper;
import com.meme.vo.MemeVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemeService {

    private final MemeMapper memeMapper;
    private final TagMapper tagMapper;

    public MemeService(MemeMapper memeMapper, TagMapper tagMapper) {
        this.memeMapper = memeMapper;
        this.tagMapper = tagMapper;
    }

    public void save(String url, Integer userId) {
        memeMapper.insert(url, userId);
    }

    public MemeVO getMeme(Integer id) {
        MemeVO meme = memeMapper.getMeme(id);
        meme.setTags(tagMapper.getTagsByMemeId(meme.getId()));
        return meme;
    }

    public List<MemeVO> pageQuery(MemePageQueryDTO memePageQueryDTO) {
        PageHelper.startPage(memePageQueryDTO.page(), memePageQueryDTO.pageSize());

        Page<MemeVO> memePage = memeMapper.pageQuery(memePageQueryDTO.userId());
        List<Integer> memeIds = memePage.getResult().stream().map(MemeVO::getId).toList();
        if (!memeIds.isEmpty()) {
            Map<Integer, List<Tag>> memeTags = tagMapper.getTagsByMemeIds(memeIds);
            for (MemeVO meme : memePage.getResult()) {
                meme.setTags(memeTags.get(meme.getId()));
            }
        }
        return memePage.getResult();
    }
}
