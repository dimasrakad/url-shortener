package com.study.url_shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.url_shortener.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
}
