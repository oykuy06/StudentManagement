package com.oyku.SpringStarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.oyku.SpringStarter")
public class SpringStarterApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringStarterApplication.class, args);
	}
}

