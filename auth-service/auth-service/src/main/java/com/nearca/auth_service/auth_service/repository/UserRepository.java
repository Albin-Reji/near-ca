package com.nearca.auth_service.auth_service.repository;

import com.nearca.auth_service.auth_service.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    boolean existsByEmail(String email);
}
