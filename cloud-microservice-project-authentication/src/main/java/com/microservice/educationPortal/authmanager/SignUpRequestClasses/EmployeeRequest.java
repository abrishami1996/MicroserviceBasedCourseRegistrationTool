package com.microservice.educationPortal.authmanager.SignUpRequestClasses;

public class EmployeeRequest{

    private long employeeNumber;
    private String FirstName;
    private String LastName;
    private String Password;

    public EmployeeRequest(long employeeNumber, String firstName, String lastName, String password) {
        this.employeeNumber = employeeNumber;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
    }
    public EmployeeRequest()
    {

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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
