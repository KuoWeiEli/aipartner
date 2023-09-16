package com.owl.aipartner.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SendMailRequest {
    @NotEmpty
    private String type;
    @NotEmpty
    private String subject;
    @NotEmpty
    private String content;
    @NotEmpty
    private String[] receivers;
}
