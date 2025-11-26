package com.oyku.SpringStarter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.oyku.SpringStarter")
@OpenAPIDefinition(
		info = @Info(
				title = "SpringStarter API",
				version = "1.0",
				description = "API documentation for SpringStarter project"
		)
)
public class SpringStarterApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringStarterApplication.class, args);
	}
}
