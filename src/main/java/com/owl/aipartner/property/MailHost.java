package com.owl.aipartner.property;

import lombok.Data;

@Data
public class MailHost {
    private String host;
    private int port;
    private String username;
    private String password;
}