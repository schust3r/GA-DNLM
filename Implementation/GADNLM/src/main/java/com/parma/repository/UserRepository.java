package com.parma.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parma.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}