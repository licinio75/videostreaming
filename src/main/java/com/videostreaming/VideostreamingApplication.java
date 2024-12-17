package com.videostreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.videostreaming")
public class VideostreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideostreamingApplication.class, args);
	}

}
