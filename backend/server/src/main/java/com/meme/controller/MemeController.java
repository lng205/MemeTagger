package com.meme.controller;

import com.meme.dto.MemePageQueryDTO;
import com.meme.result.PageResult;
import com.meme.result.Result;
import com.meme.service.MemeService;
import com.meme.vo.MemeVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meme")
public class MemeController {

    private final MemeService memeService;

    public MemeController(MemeService memeService) {
        this.memeService = memeService;
    }

    @GetMapping("/{id}")
    public Result<MemeVO> getMemeById(@PathVariable("id") Integer id) {
        return Result.success(memeService.getMeme(id));
    }

    @GetMapping
    public Result<PageResult<MemeVO>> getUserMemePage(MemePageQueryDTO memePageQueryDTO) {
        PageResult<MemeVO> memes = memeService.getMemePageByUser(memePageQueryDTO);
        return Result.success(memes);
    }
}
