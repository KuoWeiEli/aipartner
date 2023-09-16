package com.owl.aipartner.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.owl.aipartner.property.MailHost;
import com.owl.aipartner.property.MailProperties;
import com.owl.aipartner.service.MailService;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {
    public static final String OFFICIAL_MAIL_SERVICE = "officialMailService";
    public static final String SYSTEM_MAIL_SERVICE = "systemMailService";

    @Autowired
    private MailProperties mailProps;

    @Bean(OFFICIAL_MAIL_SERVICE)
    public MailService officialMailService() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        configureHost(mailSender, mailProps.getOfficial());
        configureMailProps(mailSender.getJavaMailProperties());
        return new MailService(mailSender);
    }

    @Bean(SYSTEM_MAIL_SERVICE)
    public MailService systemMailService() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        configureHost(mailSender, mailProps.getSystem());
        configureMailProps(mailSender.getJavaMailProperties());
        return new MailService(mailSender);
    }

    private void configureHost(JavaMailSenderImpl mailSender, MailHost host) {
        mailSender.setHost(host.getHost());
        mailSender.setPort(host.getPort());
        mailSender.setUsername(host.getUsername());
        mailSender.setPassword(host.getPassword());
    }

    private void configureMailProps(Properties props) {
        props.put("mail.smtp.auth", mailProps.isAuthEnabled());
        props.put("mail.smtp.starttls.enable", mailProps.isStarttlsEnabled());
        props.put("mail.transport.protocol", mailProps.getProtocol());
    }
}
