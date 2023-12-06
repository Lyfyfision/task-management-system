package com.systemproject.taskmanagement.security;

import com.systemproject.taskmanagement.security.manager.CustomAuthManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private AuthEntryPoint authEntryPoint;
    private CustomAuthManager authManager;
}
