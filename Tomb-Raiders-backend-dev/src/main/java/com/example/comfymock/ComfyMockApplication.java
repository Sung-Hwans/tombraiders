package com.example.comfymock;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComfyMockApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ComfyMockApplication.class);

		app.setDefaultProperties(Collections.singletonMap("server.port", "8188"));

		app.run(args);
	}
}
