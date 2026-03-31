package com.Qr.Qr.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ Disable CORS handling in Spring Security
                // Our CorsFilter handles it instead
                .cors(cors -> cors.disable())

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
			.requestMatchers("/api/auth/**").permitAll()
            
            // Super Admin - Full access to everything
            .requestMatchers("/api/super-admin/**").hasRole("SUPER_ADMIN")
            
            // Admin - Cannot manage super admins
            .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
            
            .requestMatchers("/api/teachers/**").hasAnyRole("TEACHER", "ADMIN", "SUPER_ADMIN")
            .requestMatchers("/api/students/**").hasAnyRole("STUDENT", "ADMIN", "SUPER_ADMIN")
            .requestMatchers("/api/courses/**").hasAnyRole("TEACHER", "STUDENT", "ADMIN", "SUPER_ADMIN")
            .requestMatchers("/api/qr/**").hasAnyRole("TEACHER", "ADMIN", "SUPER_ADMIN")
            .requestMatchers("/api/attendance/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN", "SUPER_ADMIN")
            .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
