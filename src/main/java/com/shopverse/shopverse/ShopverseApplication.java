package com.shopverse.shopverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class ShopverseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopverseApplication.class, args);
	}

}
