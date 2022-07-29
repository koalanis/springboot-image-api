package com.koalanis.imageapi.service;

import com.koalanis.imageapi.models.api.ImageMetadata;
import com.koalanis.imageapi.models.api.ImageUploadRequest;

import java.util.List;

public interface ImageService {

    ImageMetadata handleUploadRequest(ImageUploadRequest uploadRequest);

    List<ImageMetadata> getAllImages();

    List<ImageMetadata> getAllImagesWithTags(List<String> tags);

    ImageMetadata getImageById(String id);
}
