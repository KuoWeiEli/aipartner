package com.owl.aipartner.model.dto;

import lombok.Data;

@Data
public class UserQueryParameter {
    private String name;
    private Integer ageFrom;
    private Integer ageTo;
    private String orderBy;
    private String sortBy;
}
