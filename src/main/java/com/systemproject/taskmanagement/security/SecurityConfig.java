package com.systemproject.taskmanagement.security;

import com.systemproject.taskmanagement.security.filters.AuthFilter;
import com.systemproject.taskmanagement.security.filters.JwtAuthorizationFilter;
import com.systemproject.taskmanagement.security.manager.CustomAuthManager;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.systemproject.taskmanagement.security.SecurityConstants.LOGIN_PATH;
import static com.systemproject.taskmanagement.security.SecurityConstants.REGISTER_PATH;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private AuthEntryPoint authEntryPoint;
    @Autowired
    private CustomAuthManager authManager;
    @Setter
    private AuthFilter authFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        authFilter = new AuthFilter(authManager);
        return http
                .formLogin(form -> form
                        .usernameParameter("email")
                        .permitAll())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, LOGIN_PATH, REGISTER_PATH).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc -> exc.authenticationEntryPoint(authEntryPoint))
                .addFilter(authFilter)
                .addFilterAfter(new JwtAuthorizationFilter(), AuthFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(false);

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
