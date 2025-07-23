package com.GIRA.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the GIRA Spring Boot application.
 * <p>
 * Bootstraps the airport complaint management backend service.
 * </p>
 *
 * @author Mohamed yahya jabrane
 * @since 1.0
 */
@SpringBootApplication
@EnableScheduling
public class GiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiraApplication.class, args);
	}

}
