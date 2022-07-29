package com.koalanis.imageapi.service.impl;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.koalanis.imageapi.errors.ServiceException;
import com.koalanis.imageapi.service.ImageAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("googleImageAnalysisServiceImpl")
@Slf4j
public class GoogleImageAnalysisServiceImpl implements ImageAnalysisService {

    @Override
    public List<String> analyzeObjectsInImage(byte[] imageData) {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.copyFrom(imageData);
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(img).build();
            requests.add(request);

            // Performs object detection
            BatchAnnotateImagesResponse batchResponse = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = batchResponse.getResponsesList();

            // if there are no responses, then return no tags
            if(responses.isEmpty()) {
                log.warn("Google Image Analysis service did not return a response");
                return Collections.emptyList();
            }

            AnnotateImageResponse annotateImageResponse = responses.get(0);
            // map all EntityAnnotations into their image detection labels
            return annotateImageResponse.getLabelAnnotationsList()
                    .stream()
                    .map(EntityAnnotation::getDescription)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
