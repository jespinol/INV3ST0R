package com.jmel.inv3st0r;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class Inv3st0rApplication {
	public static void main(String[] args) {
		SpringApplication.run(Inv3st0rApplication.class, args);
	}

}
