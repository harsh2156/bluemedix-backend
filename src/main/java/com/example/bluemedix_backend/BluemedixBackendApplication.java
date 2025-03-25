package com.example.bluemedix_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.bluemedix_backend.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
public class BluemedixBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BluemedixBackendApplication.class, args);
	}

}
