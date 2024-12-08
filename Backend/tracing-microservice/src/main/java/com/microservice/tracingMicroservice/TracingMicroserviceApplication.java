package com.microservice.tracingMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TracingMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TracingMicroserviceApplication.class, args);
	}

}
