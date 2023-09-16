package com.owl.aipartner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.owl.aipartner.config.MailConfig;
import com.owl.aipartner.model.dto.SendMailRequest;
import com.owl.aipartner.service.MailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/mail", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailController {

    @Autowired
    @Qualifier(MailConfig.OFFICIAL_MAIL_SERVICE)
    private MailService officialMailService;

    @Autowired
    @Qualifier(MailConfig.SYSTEM_MAIL_SERVICE)
    private MailService systemMailService;

    @PostMapping
    public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) {
        request.setSubject("[" + request.getType() + "]" + request.getSubject());
        if ("official".equals(request.getType())) {
            officialMailService.sendMail(request);
        } else {
            systemMailService.sendMail(request);
        }
        return ResponseEntity.noContent().build();
    }
}
