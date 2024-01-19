package com.example.auth.authService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.auth.authService.service.MyUserDetailsService;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/authenticate", "/auth/register", "/auth/validateToken").permitAll()
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .userDetailsService(userDetailsService)
            .authenticationManager(authenticationManagerBean());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authentication -> {
            String username = authentication.getName();
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        };
    }
}

