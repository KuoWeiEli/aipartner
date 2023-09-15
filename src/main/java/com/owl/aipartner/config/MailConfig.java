package com.owl.aipartner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration

@ConfigurationProperties("mail")
@EnableConfigurationProperties(MailConfig.class)
@Data
public class MailConfig {
    private String host;
    private int port;
    private boolean authEnabled;
    private boolean starttlsEnabled;
    private String protocol;
    private String username;
    private String password;
}
