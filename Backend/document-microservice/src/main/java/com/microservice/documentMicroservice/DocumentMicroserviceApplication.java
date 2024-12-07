package com.microservice.documentMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DocumentMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentMicroserviceApplication.class, args);
	}

}
