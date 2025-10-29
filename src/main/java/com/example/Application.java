package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Spring Boot OpenAPI Generator project.
 * <p>
 * This application demonstrates the integration of:
 * </p>
 * <ul>
 *   <li>Spring Boot 3.5.7 with Java 21</li>
 *   <li>OpenAPI Generator for API-first development</li>
 *   <li>RFC 7807 Problem Details for standardized error responses</li>
 *   <li>Bean validation (JSR-303/JSR-380)</li>
 * </ul>
 *
 * @author Spring Boot OpenAPI Generator
 * @version 0.0.1-SNAPSHOT
 * @since 1.0
 */
@SpringBootApplication
public class Application {

	/**
	 * Main entry point for the Spring Boot application.
	 * <p>
	 * Initializes and starts the Spring Boot application context with
	 * auto-configuration enabled.
	 * </p>
	 *
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
