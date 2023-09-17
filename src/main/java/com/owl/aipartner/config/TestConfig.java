// package com.owl.aipartner.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.owl.aipartner.repository.mongo.UserRepository;

// import lombok.extern.slf4j.Slf4j;

// @Configuration
// @Slf4j
// public class TestConfig {

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private MailConfig mailConfig;

//     @Bean
//     public CommandLineRunner test() {
//         return args -> {
//             // log.info("Count: " + userRepository.countByIdIn(List.of(2L, 3L)));
//             // log.info("exists is: " + userRepository.existsByIdIn(List.of(4L, 5L)));
//             // log.info("result: " + userRepository.findByCustomQuery(22, 22, "a"));

//             // log.info(mailConfig.getHost());
//         };
//     }
// }
