CREATE TABLE IF NOT EXISTS image (
    id INT(12) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(30) NOT NULL,
    object_detection_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    file_path VARCHAR(1000) NOT NULL
    );


CREATE TABLE IF NOT EXISTS image_tag (
    id INT(12) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fk_image_id INT(12) UNSIGNED NOT NULL,
    tag VARCHAR(255) NOT NULL,
    FOREIGN KEY (fk_image_id) REFERENCES image(id)
    );