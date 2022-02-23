package com.microservice.educationPortal.courseManager.model;

import com.microservice.educationPortal.courseManager.exceptions.NotProvidedInitialValueException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="Courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private int code;

    @Column
    private String name;

    @Column
    private int creditHour;

    @Column
    private String department;

    @Column
    private int year;

    @Column
    private Semester semester;

    @Column
    private String classroom;

    @Column
    private int professorId;

    public Course(int code, String name, int creditHour, String department, int year, Semester semester, String classroom, int professorId) throws NotProvidedInitialValueException {
        if(name == null || semester == null){
            throw new NotProvidedInitialValueException();
        }
        if("".equals(name) || "".equals(semester.toString())){
            throw new NotProvidedInitialValueException();
        }
        if (code == 0 || year ==0){
            throw new NotProvidedInitialValueException();
        }
        this.year = year;
        this.code = code;
        this.name = name;
        this.creditHour = creditHour;
        this.department = department;
        this.semester = semester;
        this.classroom = classroom;
        this.professorId = professorId;
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        this.name = name;
    }

    public int getCreditHour() {
        return creditHour;
    }

    public void setCreditHour(int creditHour) {
        this.creditHour = creditHour;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        Objects.requireNonNull(semester, "semester must not be null");
        this.semester = semester;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getCode() { return code; }

    public void setCode(int code) {
        Objects.requireNonNull(code, "code must not be null");
        this.code = code;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

//    @Override
//    public String toString() {
//
//        return "Course:" + this.id +":" + this.name;
//    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        Objects.requireNonNull(year, "year must not be null");
        this.year = year;
    }


}
