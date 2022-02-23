package com.microservice.educationPortal.authmanager.repository;

import com.microservice.educationPortal.authmanager.Model.*;
import com.microservice.educationPortal.authmanager.Model.FacultyEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface FacultyEmployeeRepository extends JpaRepository<FacultyEmployee, Integer> {
    boolean existsByEmployeeNumber(long employeenumber);
    boolean existsById(int ID);
    @Transactional
    void deleteByEmployeeNumber(long employeenumber);
    @Transactional
    void deleteById(int Id);
    FacultyEmployee findById(int fk);
}
