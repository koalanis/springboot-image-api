package com.koalanis.imageapi.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class ImageUploadRequest {

    @JsonProperty("imageData")
    private String imageData;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("imageLabel")
    private String imageLabel;

    @JsonProperty("enableObjectDetection")
    private boolean enableObjectDetection = false;

}
