package com.koalanis.imageapi.service.impl;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.koalanis.imageapi.service.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service("googleImageStorageServiceImpl")
public class GoogleImageStorageServiceImpl implements ImageStorageService {

    @Value("${imageapi.storage.bucketname}")
    private String bucketName;

    @Value("${imageapi.storage.imagefolder}")
    private String imageFolderName;

    @Value("${imageapi.googleprojectid}")
    private String googleProjectId;


    @Override
    public String storeImage(String prefixName, byte[] imageData) {

        String prefix = "image";
        if(StringUtils.isNotBlank(prefixName)) {
            prefix = prefixName;
        }

        log.info("projectname={}, bucketname={}, imagefoldername={}", googleProjectId, bucketName, imageFolderName);

        long unixTimestamp = Instant.now().getEpochSecond();

        String imageObjectName = String.format("%s-%s-%s", prefix, unixTimestamp, RandomStringUtils.randomAlphanumeric(8));
        String objectPath = String.format("%s/%s", imageFolderName, imageObjectName);

        Storage storage = StorageOptions.newBuilder().setProjectId(googleProjectId).build().getService();

        BlobId blobId = BlobId.of(bucketName, objectPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, imageData);

        return objectPath;
    }
}
