package com.microservice.educationPortal.authmanager.Model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Student")
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "StudentNumber")
    private long studentNumber;

    @Column(name = "FirstName")
    private String FirstName;

    @Column(name = "LastName")
    private String LastName;

    @Column(name = "Major")
    private String Major;

    @Column(name = "EntranceYear")
    private int EntranceYear;

    @Column(name = "EducationalState")
    private String EducationalState;

//    @Column(name = "PassWord")
//    private String Password;


    public Student(long studentNumber, String firstName, String lastName, String major, int entranceYear, String  educationalState ) {
        this.studentNumber = studentNumber;
        FirstName = firstName;
        LastName = lastName;
        Major = major;
        EntranceYear = entranceYear;
        EducationalState = educationalState;
//        Password = password;
    }

    public Student()
    {

    }

    public int getId() {
        return id;
    }


    public long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(long studentNumber) {
        this.studentNumber = studentNumber;
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

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public int getEntranceYear() {
        return EntranceYear;
    }

    public void setEntranceYear(int entranceYear) {
        EntranceYear = entranceYear;
    }

    public String getEducationalState() {
        return EducationalState;
    }

    public void setEducationalState(String educationalState) {
        EducationalState = educationalState;
    }

//    public String getPassword() {
//        return Password;
//    }
//
//    public void setPassword(String password) {
//        Password = password;
//    }
}
