package com.hotelvista.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // sử dụng CorsConfigurationSource bean
                .csrf(csrf -> csrf.disable()) // nếu dùng JWT, thường disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow preflight
                        .requestMatchers(HttpMethod.GET, "/api/rooms").permitAll() // public list endpoint
                        .requestMatchers("/api/rooms", "/api/rooms/**").permitAll() // public endpoint
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/room-types", "/api/room-types/**").permitAll()
                        .anyRequest().authenticated()
                );
        // thêm các filter JWT của bạn ở đây nếu cần
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // CHÚ Ý: nếu allowCredentials(true) thì không dùng "*" cho origins
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://192.168.111.200:5173",
                "http://192.168.111.200:3000",
                "http://192.168.111.200:8080",
                "http://192.168.110.139:5173",
                "https://vigorous-jannet-indefinable.ngrok-free.dev"

        ));
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","Accept","X-Requested-With"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}