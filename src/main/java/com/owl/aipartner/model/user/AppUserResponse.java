package com.owl.aipartner.model.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponse {
    private String id;
    private String name;
    private int age;
    private String email;
    private List<UserAuthority> UserAuthorities;
}
