package com.owl.aipartner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.owl.aipartner.repository.mongo")
@EnableJpaRepositories(basePackages = "com.owl.aipartner.repository.h2")
@Configuration
public class JpaConfig {

}
