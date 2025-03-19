package com.meme.controller;

import com.meme.result.Result;
import com.meme.service.MemeService;
import com.meme.utils.GoogleCloudStorageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    private final GoogleCloudStorageUtils googleCloudStorageUtils;
    private final MemeService memeService;
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    public CommonController(GoogleCloudStorageUtils googleCloudStorageUtils, MemeService memeService) {
        this.googleCloudStorageUtils = googleCloudStorageUtils;
        this.memeService = memeService;
    }

    @PostMapping(value = "/upload", produces = "application/json")
    public Result<String> upload(@RequestPart("file") MultipartFile file,
                                 @AuthenticationPrincipal Jwt principal) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String extension = fileName.substring(fileName.lastIndexOf("."));
        // TODO check file extension
        // TODO check file size
        String objectName = UUID.randomUUID() + "." + extension;

        try {
            String url = googleCloudStorageUtils.uploadImage(objectName, file.getBytes());
            Long userId = principal.getClaim("id");
            memeService.save(url, userId.intValue());
            return Result.success(url);
        } catch (IOException e) {
            logger.error("Upload file failed", e);
            throw new RuntimeException(e);
        }
    }
}
