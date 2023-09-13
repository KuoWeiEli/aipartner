package com.owl.aipartner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.owl.aipartner.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameIgnoreCaseLike(String name);
}
