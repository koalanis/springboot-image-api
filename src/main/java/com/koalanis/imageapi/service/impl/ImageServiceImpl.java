package com.koalanis.imageapi.service.impl;

import com.koalanis.imageapi.errors.BadRequestException;
import com.koalanis.imageapi.errors.ImageNotFoundException;
import com.koalanis.imageapi.errors.ServiceException;
import com.koalanis.imageapi.models.api.ImageMetadata;
import com.koalanis.imageapi.models.api.ImageUploadRequest;
import com.koalanis.imageapi.models.db.ImageData;
import com.koalanis.imageapi.models.db.ImageTag;
import com.koalanis.imageapi.repository.ImageDataRepo;
import com.koalanis.imageapi.repository.ImageTagRepo;
import com.koalanis.imageapi.service.ImageAnalysisService;
import com.koalanis.imageapi.service.ImageService;
import com.koalanis.imageapi.service.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("imageServiceImpl")
public class ImageServiceImpl implements ImageService {

    @Resource(name="googleImageAnalysisServiceImpl")
    ImageAnalysisService imageAnalysisService;

    @Resource(name="googleImageStorageServiceImpl")
    ImageStorageService imageStorageService;

    @Resource(name="imageDataRepo")
    ImageDataRepo imageDataRepo;

    @Resource(name="imageTagRepo")
    ImageTagRepo imageTagRepo;

    @Override
    public ImageMetadata handleUploadRequest(ImageUploadRequest uploadRequest) {

        // check for image data
        if(StringUtils.isBlank(uploadRequest.getImageData()) && StringUtils.isBlank(uploadRequest.getImageUrl())) {
            throw new BadRequestException("Neither image data nor an image url was supplied.");
        }

        byte[] rawImageData = getRawImageBytes(uploadRequest);

        // resolve label of image
        String imageLabel = "image";
        if(StringUtils.isNotBlank(uploadRequest.getImageLabel())) {
            imageLabel = uploadRequest.getImageLabel();
        }

        // save image in cloud storage
        String storagePath = imageStorageService.storeImage(imageLabel, rawImageData);

        // perform object detection if enabled
        List<String> tags = uploadRequest.isEnableObjectDetection() ?
                imageAnalysisService.analyzeObjectsInImage(rawImageData) :
                Collections.emptyList();

        // save image entity
        ImageData.ImageDataBuilder imageDataBuilder = ImageData.builder();
        imageDataBuilder.imageLabel(imageLabel);
        imageDataBuilder.storagePath(storagePath);
        imageDataBuilder.objectDetectionEnabled(uploadRequest.isEnableObjectDetection());
        ImageData imageData = imageDataBuilder.build();
        imageData = imageDataRepo.save(imageData);

        // perform object detection and save image tags

        if(!tags.isEmpty()) {
            for(String tag : tags) {
                ImageTag imageTag = ImageTag.builder()
                        .imageData(imageData)
                        .tag(tag)
                        .build();
                imageTagRepo.save(imageTag);
            }
        }

        ImageMetadata imageMetadata  = ImageMetadata.builder()
                .imageData(getBase64FromBytes(rawImageData))
                .imageId(imageData.getId().toString())
                .imageStoragePath(imageData.getStoragePath())
                .imageLabel(imageData.getImageLabel())
                .imageTags(tags)
                .build();

        return imageMetadata;
    }

    @Override
    public List<ImageMetadata> getAllImages() {
        List<ImageData> images = imageDataRepo.findAll();
        return images.stream().map(this::fromImageDataToImageMetadata).collect(Collectors.toList());
    }

    @Override
    public List<ImageMetadata> getAllImagesWithTags(List<String> tags) {
        List<ImageData> images = imageTagRepo.findImageDataByTags(tags);
        return images.stream().map(this::fromImageDataToImageMetadata).collect(Collectors.toList());
    }

    @Override
    public ImageMetadata getImageById(String strId) {
        int id = 0;
        try{
            id = Integer.parseInt(strId);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Invalid image id.");
        }

        ImageData imageData = null;

        if(!imageDataRepo.existsById(id)) {
            throw new ImageNotFoundException();
        } else {
            imageData = imageDataRepo.getById(id);
        }

        return fromImageDataToImageMetadata(imageData);
    }

    private ImageMetadata fromImageDataToImageMetadata(ImageData imageData) {
        if(imageData == null) {
            return ImageMetadata.builder().build();
        }
        return ImageMetadata.builder()
                  .imageTags(imageData.getTags().stream().map(ImageTag::getTag).collect(Collectors.toList()))
                  .imageStoragePath(imageData.getStoragePath())
                  .imageId(imageData.getId().toString())
                  .imageLabel(imageData.getImageLabel())
                  .build();
    }

    private byte[] getRawImageBytes(ImageUploadRequest uploadRequest) {
        if(StringUtils.isNotBlank(uploadRequest.getImageData())) {
            return Base64.decodeBase64(uploadRequest.getImageData());
        }
        String imageUrl = uploadRequest.getImageUrl();
        try {
            URL url = new URL(imageUrl);
            try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream())) {
                return inputStream.readAllBytes();
            }
        } catch (MalformedURLException e) {
            throw new BadRequestException("Image Url is malformed.");
        } catch (IOException e) {
            throw new ServiceException(String.format("Error occurred during image download from url=%s", imageUrl));
        }
    }

    private String getBase64FromBytes(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }
}
