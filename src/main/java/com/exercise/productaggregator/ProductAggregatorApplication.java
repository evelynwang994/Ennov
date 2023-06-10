package com.exercise.productaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ProductAggregatorApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ProductAggregatorApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ProductAggregatorApplication.class);
	}

}
