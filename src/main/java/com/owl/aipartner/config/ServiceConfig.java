package com.owl.aipartner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.owl.aipartner.repository.mongo.UserRepository;
import com.owl.aipartner.service.UserService;

@Configuration
public class ServiceConfig {

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserService(userRepository, passwordEncoder);
    }
}
