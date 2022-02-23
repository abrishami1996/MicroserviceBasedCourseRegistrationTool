package com.microservice.educationPortal.authmanager.SignUpRequestClasses;


public class ProfessorRequest{

    private long professorNumber;
    private String FirstName;
    private String LastName;
    private String Department;
    private String Position;
    private String Field;
    private String Password;

    public ProfessorRequest(long professorNumber, String firstName, String lastName, String department, String position, String field , String password) {
        this.professorNumber = professorNumber;
        FirstName = firstName;
        LastName = lastName;
        Department = department;
        Position = position;
        Field = field;
        Password = password;
    }
    public ProfessorRequest()
    {

    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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
