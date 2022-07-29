package com.koalanis.imageapi.models.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Builder
@Table(name = "image_tag")
@NoArgsConstructor
@AllArgsConstructor
public class ImageTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_image_id")
    private ImageData imageData;

    @Column(name = "tag")
    @NotEmpty
    private String tag;
}
