package com.meme.controller;

import com.meme.result.Result;
import com.meme.utils.GoogleCloudStorageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    GoogleCloudStorageUtils googleCloudStorageUtils;
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    public CommonController(GoogleCloudStorageUtils googleCloudStorageUtils) {
        this.googleCloudStorageUtils = googleCloudStorageUtils;
    }

    @PostMapping(value = "/upload", produces = "application/json")
    public Result<String> upload(@RequestPart("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String objectName = UUID.randomUUID() + "." + extension;

        try {
            String filePath = googleCloudStorageUtils.uploadImage(objectName, file.getBytes());
            return Result.success(filePath);
        } catch (IOException e) {
            logger.error("Upload file failed", e);
            throw new RuntimeException(e);
        }
    }
}
