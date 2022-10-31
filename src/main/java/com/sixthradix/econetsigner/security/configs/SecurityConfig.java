package com.sixthradix.econetsigner.security.configs;

import com.sixthradix.econetsigner.security.filters.CustomAuthenticationFilter;
import com.sixthradix.econetsigner.security.providers.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider authProvider;
    private final AuthenticationManagerBuilder authManagerBuilder;

    public SecurityConfig(CustomAuthenticationProvider authProvider, AuthenticationManagerBuilder authManagerBuilder) {
        this.authProvider = authProvider;
        this.authManagerBuilder = authManagerBuilder;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterAt(new CustomAuthenticationFilter(authManagerBuilder.getOrBuild()), BasicAuthenticationFilter.class);
        http.csrf().disable().authorizeRequests()
                .anyRequest()
                .permitAll();
        return http.build();
    }
}
