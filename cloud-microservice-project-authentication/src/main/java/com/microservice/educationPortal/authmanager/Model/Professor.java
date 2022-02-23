package com.microservice.educationPortal.authmanager.Model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="professor")
public class Professor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ProfessorNumber")
    private long professorNumber;
    @Column(name = "FirstName")
    private String FirstName;
    @Column(name = "LastName")
    private String LastName;
    @Column(name = "Department")
    private String Department;
    @Column(name = "Position")
    private String Position;
    @Column(name = "Field")
    private String Field;
//    @Column(name = "Password")
//    private String Password;

    public Professor(long professorNumber, String firstName, String lastName, String department, String position, String field ) {
        this.professorNumber = professorNumber;
        FirstName = firstName;
        LastName = lastName;
        Department = department;
        Position = position;
        Field = field;
//        Password = password;
    }
    public Professor()
    {

    }
//
//    public String getPassword() {
//        return Password;
//    }
//
//    public void setPassword(String password) {
//        Password = password;
//    }

    public int getId() {
        return id;
    }


    public long getProfessorNumber() {
        return professorNumber;
    }

    public void setProfessorNumber(long professorNumber) {
        this.professorNumber = professorNumber;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }


}
