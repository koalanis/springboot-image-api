package com.koalanis.imageapi.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class ImageMetadata {

    @JsonProperty("imageId")
    private String imageId;

    @JsonProperty("imageLabel")
    private String imageLabel;

    @JsonProperty("imageTags")
    private List<String> imageTags;

    @JsonProperty("imageStoragePath")
    private String imageStoragePath;

    @JsonProperty("imageData")
    private String imageData;
}
