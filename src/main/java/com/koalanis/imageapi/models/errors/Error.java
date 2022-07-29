package com.koalanis.imageapi.models.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Error {
    @JsonProperty("errorMessage")
    private String errorMessage;
}
