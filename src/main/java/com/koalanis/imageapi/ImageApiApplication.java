package com.koalanis.imageapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.koalanis.imageapi.repository")
public class ImageApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageApiApplication.class, args);
	}

}
