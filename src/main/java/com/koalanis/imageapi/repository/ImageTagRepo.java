package com.koalanis.imageapi.repository;

import com.koalanis.imageapi.models.db.ImageData;
import com.koalanis.imageapi.models.db.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository("imageTagRepo")
public interface ImageTagRepo extends JpaRepository<ImageTag, Integer> {

    @Transactional
    @Query("SELECT DISTINCT it.imageData FROM ImageTag it WHERE it.tag IN ?1")
    List<ImageData> findImageDataByTags(List<String> tags);
}
