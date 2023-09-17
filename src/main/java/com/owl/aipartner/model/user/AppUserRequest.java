package com.owl.aipartner.model.user;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequest {
    @NotEmpty
    private String name;
    @Min(0)
    @Max(200)
    private int age;

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotEmpty
    private List<UserAuthority> authorities;
}
