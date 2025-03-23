package com.meme.controller;

import com.meme.entity.Tag;
import com.meme.result.Result;
import com.meme.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/{memeId}")
    public Result setMemeTags(@PathVariable("memeId") Integer id, @RequestBody List<String> tags) {
        // TODO verify userId

        tagService.setMemeTags(id, tags);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<List<Tag>> getTagsByMemeId(@PathVariable("id") Integer id) {
        return Result.success(tagService.getTagsByMemeId(id));
    }
}
