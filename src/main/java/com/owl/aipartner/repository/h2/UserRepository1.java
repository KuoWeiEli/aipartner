package com.owl.aipartner.repository.h2;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.owl.aipartner.model.User;

public interface UserRepository1 extends JpaRepository<User, Long> {
    List<User> findByNameIgnoreCaseLike(String name);
}
