package com.koalanis.imageapi.controller;

import com.koalanis.imageapi.models.api.ImageMetadata;
import com.koalanis.imageapi.models.api.ImageUploadRequest;
import com.koalanis.imageapi.service.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping
    public List<ImageMetadata> getAllImages(@RequestParam("objects") Optional<String> objects) {
        List<String> tags = getListOfTags(objects);
        if(tags.isEmpty()) {
            return imageService.getAllImages();
        }
        return imageService.getAllImagesWithTags(tags);
    }

    @GetMapping("/{imageId}")
    public ImageMetadata getImage(@PathVariable("imageId") String imageId) {
        return imageService.getImageById(imageId);
    }

    @PostMapping
    public ImageMetadata processImage(@RequestBody ImageUploadRequest uploadRequest) {
        return imageService.handleUploadRequest(uploadRequest);
    }

    private List<String> getListOfTags(Optional<String> objects) {
        if(objects.isPresent()) {
            String tagList = objects.get();
            String[] tags = tagList.split(",");
            return Arrays.stream(tags).map(String::toLowerCase).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
