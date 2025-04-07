package com.meme.controller;

import com.meme.result.Result;
import com.meme.service.MemeVoteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meme/vote")
public class MemeVoteController {

    private final MemeVoteService memeVoteService;

    public MemeVoteController(MemeVoteService memeVoteService) {
        this.memeVoteService = memeVoteService;
    }

    @PostMapping("/{memeId}")
    public Result<Boolean> toggleVote(@PathVariable("memeId") Integer memeId) {
        boolean liked = memeVoteService.toggleVote(memeId);
        return Result.success(liked);
    }
}
