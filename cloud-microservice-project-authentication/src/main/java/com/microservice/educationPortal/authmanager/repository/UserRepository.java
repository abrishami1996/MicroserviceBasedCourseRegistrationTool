package com.microservice.educationPortal.authmanager.repository;

import com.microservice.educationPortal.authmanager.Model.*;
import com.microservice.educationPortal.authmanager.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String Username,String Password);
    User findByUsername(String Username);
    boolean existsByUsername(String username);

    @Transactional
    void deleteByUsername(String Username);
}
