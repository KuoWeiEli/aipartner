package com.owl.aipartner.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("mail")
public class MailProperties {
    private boolean authEnabled;
    private boolean starttlsEnabled;
    private String protocol;

    private MailHost official;
    private MailHost system;
}
