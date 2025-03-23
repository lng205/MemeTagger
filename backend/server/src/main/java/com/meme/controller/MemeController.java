package com.meme.controller;

import com.meme.dto.MemePageQueryDTO;
import com.meme.result.Result;
import com.meme.service.MemeService;
import com.meme.vo.MemeVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result<List<MemeVO>> getUserMemeByPage(MemePageQueryDTO memePageQueryDTO) {
        return Result.success(memeService.pageQuery(memePageQueryDTO));
    }
}
