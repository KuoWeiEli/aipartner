package com.owl.aipartner.model.po;

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
public class UserPO {
    private String id;
    private String name;
    private int age;
}
