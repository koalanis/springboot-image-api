package com.koalanis.imageapi.service;

import java.util.List;

public interface ImageAnalysisService {

    List<String> analyzeObjectsInImage(byte[] imageData);
}
