package com.microservice.educationPortal.authmanager.SignUpRequestClasses;


public class StudentRequest {

        private long studentNumber;
        private String FirstName;
        private String LastName;
        private String Major;
        private int EntranceYear;
        private String EducationalState;
        private String Password;

    public StudentRequest(long studentNumber, String firstName, String lastName, String major, int entranceYear, String educationalState, String password) {
        this.studentNumber = studentNumber;
        FirstName = firstName;
        LastName = lastName;
        Major = major;
        EntranceYear = entranceYear;
        EducationalState = educationalState;
        Password = password;
    }

    public StudentRequest()
        {

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

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }
    }


