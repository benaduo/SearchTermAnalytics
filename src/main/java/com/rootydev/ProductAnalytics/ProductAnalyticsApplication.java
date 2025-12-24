package com.rootydev.ProductAnalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductAnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductAnalyticsApplication.class, args);
	}

}
