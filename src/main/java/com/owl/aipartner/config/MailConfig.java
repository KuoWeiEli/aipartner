package com.owl.aipartner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration

// @PropertySource("classpath:mail.properties")
@ConfigurationProperties("mail")
@EnableConfigurationProperties(MailConfig.class)
@Data
public class MailConfig {
    // @Value("${mail.host}")
    private String host;

    //@Value("${mail.port:25}")
    private int port;

    //@Value("${mail.enable_auth}")
    private boolean authEnabled;

    //@Value("${mail.enabled_starttls}")
    private boolean starttlsEnabled;

    //@Value("${mail.protocol}")
    private String protocol;

    //@Value("${mail.username}")
    private String username;

    //@Value("${mail.password}")
    private String password;

}
