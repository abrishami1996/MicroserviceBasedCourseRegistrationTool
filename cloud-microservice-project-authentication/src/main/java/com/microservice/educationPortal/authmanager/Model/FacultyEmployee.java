package com.microservice.educationPortal.authmanager.Model;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name= "employee")
public class FacultyEmployee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "EmployeeNumber")
    private long employeeNumber;
    @Column(name = "FirstName")
    private String FirstName;
    @Column(name = "lastName")
    private String LastName;
//    @Column(name = "Password")
//    private String Password;

    public FacultyEmployee(long employeeNumber, String firstName, String lastName) {
        this.employeeNumber = employeeNumber;
        FirstName = firstName;
        LastName = lastName;
//        Password = password;
    }
    public FacultyEmployee()
    {

    }

    public int getId() {
        return id;
    }

    public long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(long employeeNumber) {
        this.employeeNumber = employeeNumber;
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

//    public String getPassword() {
//        return Password;
//    }
//
//    public void setPassword(String password) {
//        Password = password;
//    }

}
