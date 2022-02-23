package com.microservice.educationPortal.courseSelection.repository;

import com.microservice.educationPortal.courseSelection.models.TakenCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TakenCourseRepository extends JpaRepository<TakenCourse,Integer> {
    List<TakenCourse> findByStudentNumber(long studentNumber);
    List<TakenCourse> findByCourseCode(int courseCode);

    TakenCourse findByCourseCodeAndStudentNumber(int courseCode,long studentNumber);

    void deleteByStudentNumberAndCourseCode(long studentNumber,int courseCode);
    void deleteByCourseCode(int courseCode);
}
