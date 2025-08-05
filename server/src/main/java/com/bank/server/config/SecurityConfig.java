package com.bank.server.config;

import com.bank.server.security.JwtUtil;
import com.bank.server.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/users/me").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/users").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                .requestMatchers("/api/chat").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/chat/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers("/api/friends").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/friends/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.GET,"/api/customers").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/customers").hasRole("ADMIN")
                .requestMatchers("/api/customers/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.GET,"/api/products").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/products").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/products/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/accounts").hasAnyRole("USER", "ADMIN")         // 전체 조회
                .requestMatchers(HttpMethod.POST, "/api/accounts").hasAnyRole("USER", "ADMIN")        // 계좌 생성
                .requestMatchers(HttpMethod.DELETE, "/api/accounts/**").hasRole("ADMIN")              // 단건 삭제

                .requestMatchers("/api/accounts/customer/**").hasAnyRole("USER", "ADMIN")             // 고객별 조회

                .requestMatchers("/api/accounts/{id:\\d+}/approve").hasRole("ADMIN")                  // 승인
                .requestMatchers("/api/accounts/{id:\\d+}/reject").hasRole("ADMIN")                   // 거절

                .requestMatchers(HttpMethod.GET, "/api/accounts/{id:\\d+}").hasRole("ADMIN")  

                .requestMatchers("/ws-chat/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
