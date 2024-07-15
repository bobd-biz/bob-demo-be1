package com.examples.bobd.config;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
	
	private static final long MAX_AGE = Duration.ofMinutes(10).getSeconds();

	@Bean
	CorsWebFilter corsWebFilter() {
	    CorsConfiguration corsConfig = new CorsConfiguration();
	    corsConfig.applyPermitDefaultValues();
	    corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
	    corsConfig.setMaxAge(MAX_AGE);
	    corsConfig.addAllowedMethod("PUT");
	    corsConfig.addAllowedMethod("DELETE");

	    UrlBasedCorsConfigurationSource source =
	      new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", corsConfig);

	    return new CorsWebFilter(source);
	}
}
