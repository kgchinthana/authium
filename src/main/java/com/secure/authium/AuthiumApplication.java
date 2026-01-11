package com.secure.authium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class AuthiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthiumApplication.class, args);
	}

}
