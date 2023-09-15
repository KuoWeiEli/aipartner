package com.owl.aipartner.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.owl.aipartner.config.MailConfig;
import com.owl.aipartner.model.dto.SendMailRequest;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {
    private JavaMailSenderImpl mailSender;

    @Autowired
    private MailConfig mailConfig;

    @PostConstruct
    private void init() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailConfig.isAuthEnabled());
        props.put("mail.smtp.starttls.enable", mailConfig.isStarttlsEnabled());
        props.put("mail.transport.protocol", mailConfig.getProtocol());
    }

    public void sendMail(SendMailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getUsername());
        message.setTo(request.getReceivers());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

}
