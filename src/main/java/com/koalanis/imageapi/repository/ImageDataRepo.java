package com.koalanis.imageapi.repository;

import com.koalanis.imageapi.models.db.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("imageDataRepo")
public interface ImageDataRepo extends JpaRepository<ImageData, Integer> {
}
