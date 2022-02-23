package com.microservice.educationPortal.courseSelection.models;

import javax.persistence.*;

@Entity
@Table(name="takenCourses")
public class TakenCourse {


    public int getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private long studentNumber;
    private int courseCode;


    public TakenCourse(long studentNumber, int courseCode) {
        this.studentNumber = studentNumber;
        this.courseCode = courseCode;
    }

    public TakenCourse() {
    }

    public long getStudentNumber() {
        return studentNumber;
    }

    public int getCourseCode() {
        return courseCode;
    }



    public void setStudentNumber(long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }
}
