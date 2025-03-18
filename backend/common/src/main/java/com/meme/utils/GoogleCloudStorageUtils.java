package com.meme.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class GoogleCloudStorageUtils {

    private final String bucketName;
    private final Storage storage;

    public GoogleCloudStorageUtils(
            @Value("${google.cloud.project-id}") String projectId,
            @Value("${google.cloud.bucket-name}") String bucketName,
            @Value("${google.cloud.credentials}") String credentialsPath) throws IOException {
        this.bucketName = bucketName;

        // ADC environment variable does not work
        GoogleCredentials credentials = GoogleCredentials
            .fromStream(new FileInputStream(credentialsPath));

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