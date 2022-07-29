package com.koalanis.imageapi.service;

public interface ImageStorageService {

    /**
     * Given a prefix name and image data, this service saves the image in file storage, and returns the storage name
     * for the entity.
     * @param prefixName
     * @param imageData
     * @return
     */
    String storeImage(String prefixName, byte[] imageData);
}
