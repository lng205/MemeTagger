package com.meme.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class GoogleCloudStorageUtils {

    private final String bucketName;
    private final Storage storage;

    public GoogleCloudStorageUtils(
            @Value("${cloud.google.project-id}") String projectId,
            @Value("${cloud.google.bucket-name}") String bucketName,
            @Value("${cloud.google.credentials}") String credentialsPath,
            ResourceLoader resourceLoader) throws IOException {
        this.bucketName = bucketName;

        // ADC environment variable does not work
        Resource resource = resourceLoader.getResource(credentialsPath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

        this.storage = StorageOptions
            .newBuilder()
            .setCredentials(credentials)
            .setProjectId(projectId)
            .build()
            .getService();
    }

    public String uploadImage(String objectName, String filePath) throws IOException {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpg").build();

        storage.createFrom(blobInfo, Paths.get(filePath));
        return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
    }

    public String uploadImage(String objectName, byte[] bytes) throws IOException {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpg").build();

        storage.createFrom(blobInfo, new ByteArrayInputStream(bytes));
        return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
    }
}