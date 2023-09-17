package com.owl.aipartner.model.user;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Entity
// @Table(name = "Users")
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    public AppUser(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    private String id;
    private String name;
    private int age;
    private String email;
    private String password;
    private List<UserAuthority> UserAuthorities;
}
