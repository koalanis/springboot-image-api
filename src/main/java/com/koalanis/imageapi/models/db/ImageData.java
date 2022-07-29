package com.koalanis.imageapi.models.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "image")
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "label")
    @NotEmpty
    private String imageLabel;

    @Column(name = "file_path", unique = true)
    @NotEmpty
    private String storagePath;

    @Column(name = "object_detection_enabled")
    private boolean objectDetectionEnabled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_image_id")
    @Builder.Default
    private List<ImageTag> tags = new ArrayList<>();
}
