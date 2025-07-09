package com.study.url_shortener.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.url_shortener.entities.Role;
import com.study.url_shortener.enums.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleEnum> {
    Optional<Role> findByName(RoleEnum name);
    boolean existsByName(RoleEnum name);
}
