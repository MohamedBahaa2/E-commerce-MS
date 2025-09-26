package com.ejada.shop_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShopMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopMsApplication.class, args);
	}

}
