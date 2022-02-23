package com.microservice.educationPortal.courseManager.repository;

import com.microservice.educationPortal.courseManager.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {

    Course findByCode(Integer code);

    @Transactional
    void deleteByCode(Integer code);

    List<Course> findAllByProfessorId(Integer professorId);
}
