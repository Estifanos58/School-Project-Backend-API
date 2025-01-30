package com.schoolproject.ChatAPP.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
@Configuration
public class CorsConfig {

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Allow frontend URL
        corsConfiguration.setAllowedOrigins(List.of(frontendUrl));

        // Allow credentials (important for cookies)
        corsConfiguration.setAllowCredentials(true);

        // Allow all standard HTTP methods
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allowed headers
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));

        // **IMPORTANT**: Expose `Set-Cookie` header
        corsConfiguration.setExposedHeaders(List.of("Set-Cookie"));

        // Register CORS configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}

